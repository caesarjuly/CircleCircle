package utils;


import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

public final class Threads implements ThreadsColumns {
	private static final String[] ID_PROJECTION = { BaseColumns._ID };
	private static final String STANDARD_ENCODING = "UTF-8";
	private static final Uri THREAD_ID_CONTENT_URI = Uri
			.parse("content://mms-sms/threadID");
	public static final Uri CONTENT_URI = Uri.withAppendedPath(
			Uri.parse("content://mms-sms/"), "conversations");
	public static final Uri OBSOLETE_THREADS_URI = Uri.withAppendedPath(
			CONTENT_URI, "obsolete");
	public static final Pattern NAME_ADDR_EMAIL_PATTERN = Pattern
			.compile("\\s*(\"[^\"]*\"|[^<>\"]+)\\s*<([^<>]+)>\\s*");

	public static final int COMMON_THREAD = 0;
	public static final int BROADCAST_THREAD = 1;

	// No one should construct an instance of this class.
	private Threads() {
	}

	/**
	 * This is a single-recipient version of getOrCreateThreadId. It's
	 * convenient for use with SMS messages.
	 */
	public static long getOrCreateThreadId(Context context, String recipient) {
		Set<String> recipients = new HashSet<String>();

		recipients.add(recipient);
		return getOrCreateThreadId(context, recipients);
	}

	/**
	 * Given the recipients list and subject of an unsaved message, return its
	 * thread ID. If the message starts a new thread, allocate a new thread ID.
	 * Otherwise, use the appropriate existing thread ID.
	 * 
	 * Find the thread ID of the same set of recipients (in any order, without
	 * any additions). If one is found, return it. Otherwise, return a unique
	 * thread ID.
	 */
	public static long getOrCreateThreadId(Context context,
			Set<String> recipients) {
		Uri.Builder uriBuilder = THREAD_ID_CONTENT_URI.buildUpon();

		for (String recipient : recipients) {
			if (isEmailAddress(recipient)) {
				recipient = extractAddrSpec(recipient);
			}

			uriBuilder.appendQueryParameter("recipient", recipient);
		}

		Uri uri = uriBuilder.build();
		// if (DEBUG) Log.v(TAG, "getOrCreateThreadId uri: " + uri);

		Cursor cursor = context.getContentResolver().query(uri, ID_PROJECTION,
				null, null, null);
		if (true) {
			Log.v("Threads", "getOrCreateThreadId cursor cnt: " + cursor.getCount());
		}
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					return cursor.getLong(0);
				} else {
					Log.e("Threads", "getOrCreateThreadId returned no rows!");
				}
			} finally {
				cursor.close();
			}
		}

		Log.e("Threads",
				"getOrCreateThreadId failed with uri " + uri.toString());
		throw new IllegalArgumentException(
				"Unable to find or allocate a thread ID.");
	}

	public static String extractAddrSpec(String address) {
		Matcher match = NAME_ADDR_EMAIL_PATTERN.matcher(address);

		if (match.matches()) {
			return match.group(2);
		}
		return address;
	}

	/**
	 * Returns true if the address is an email address
	 * 
	 * @param address the input address to be tested
	 * @return true if address is an email address
	 */
	public static boolean isEmailAddress(String address) {
		if (TextUtils.isEmpty(address)) {
			return false;
		}
		String s = extractAddrSpec(address);
		Matcher match = Patterns.EMAIL_ADDRESS.matcher(s);
		return match.matches();
	}
}

/**
 * Columns for the "threads" table used by MMS and SMS.
 */
interface ThreadsColumns extends BaseColumns {
	/**
	 * The date at which the thread was created.
	 * 
	 * <P>
	 * Type: INTEGER (long)
	 * </P>
	 */
	public static final String DATE = "date";

	/**
	 * A string encoding of the recipient IDs of the recipients of the
	 * message, in numerical order and separated by spaces.
	 * <P>
	 * Type: TEXT
	 * </P>
	 */
	public static final String RECIPIENT_IDS = "recipient_ids";

	/**
	 * The message count of the thread.
	 * <P>
	 * Type: INTEGER
	 * </P>
	 */
	public static final String MESSAGE_COUNT = "message_count";
	/**
	 * Indicates whether all messages of the thread have been read.
	 * <P>
	 * Type: INTEGER
	 * </P>
	 */
	public static final String READ = "read";

	/**
	 * The snippet of the latest message in the thread.
	 * <P>
	 * Type: TEXT
	 * </P>
	 */
	public static final String SNIPPET = "snippet";
	/**
	 * The charset of the snippet.
	 * <P>
	 * Type: INTEGER
	 * </P>
	 */
	public static final String SNIPPET_CHARSET = "snippet_cs";
	/**
	 * Type of the thread, either Threads.COMMON_THREAD or
	 * Threads.BROADCAST_THREAD.
	 * <P>
	 * Type: INTEGER
	 * </P>
	 */
	public static final String TYPE = "type";
	/**
	 * Indicates whether there is a transmission error in the thread.
	 * <P>
	 * Type: INTEGER
	 * </P>
	 */
	public static final String ERROR = "error";
	/**
	 * Indicates whether this thread contains any attachments.
	 * <P>
	 * Type: INTEGER
	 * </P>
	 */
	public static final String HAS_ATTACHMENT = "has_attachment";
}
