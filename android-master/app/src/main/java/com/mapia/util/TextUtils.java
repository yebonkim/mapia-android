package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 13..
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.mapia.endpage.MentionUserClickEvent;
import com.mapia.endpage.TagClickEvent;
import com.mapia.myfeed.NonUnderlineClickableSpan;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils
{
    private static ArrayList<Integer> emojiCodeValueList;
    private static final int emojiEndCodePoint1 = 128511;
    private static final int emojiEndCodePoint2 = 128591;
    private static final int emojiEndCodePoint3 = 128767;
    private static final int emojiEndCodePoint4 = 9983;
    private static final int emojiEndCodePoint5 = 10160;
    private static final int emojiEndCodePoint6 = 127569;
    private static final int emojiStartCodePoint1 = 127744;
    private static final int emojiStartCodePoint2 = 128512;
    private static final int emojiStartCodePoint3 = 128640;
    private static final int emojiStartCodePoint4 = 9728;
    private static final int emojiStartCodePoint5 = 9986;
    private static final int emojiStartCodePoint6 = 127344;

    static {
        TextUtils.emojiCodeValueList = new ArrayList<Integer>();
        final int[] array2;
        final int[] array = array2 = new int[31];
        array2[0] = 169;
        array2[1] = 174;
        array2[2] = 126980;
        array2[3] = 127183;
        array2[4] = 8252;
        array2[5] = 8617;
        array2[6] = 8618;
        array2[7] = 8986;
        array2[8] = 8987;
        array2[9] = 9194;
        array2[10] = 9195;
        array2[11] = 9196;
        array2[12] = 9200;
        array2[13] = 9203;
        array2[14] = 9410;
        array2[15] = 9642;
        array2[16] = 9643;
        array2[17] = 9654;
        array2[18] = 9664;
        array2[19] = 9723;
        array2[20] = 9724;
        array2[21] = 9725;
        array2[22] = 9726;
        array2[23] = 11013;
        array2[24] = 11014;
        array2[25] = 11015;
        array2[26] = 11035;
        array2[27] = 11036;
        array2[28] = 11088;
        array2[29] = 11093;
        array2[30] = 12349;
        for (int i = 0; i < array.length; ++i) {
            TextUtils.emojiCodeValueList.add(array[i]);
        }
        final int[] array4;
        final int[] array3 = array4 = new int[9];
        array4[0] = 8597;
        array4[1] = 8599;
        array4[2] = 8600;
        array4[3] = 8601;
        array4[4] = 10548;
        array4[5] = 10549;
        array4[6] = 8505;
        array4[7] = 12953;
        array4[8] = 12951;
        for (int j = 0; j < array3.length; ++j) {
            TextUtils.emojiCodeValueList.add(array3[j]);
        }
    }

    private static void applyStyle(final SpannableString spannableString, final int n, final int n2, final int n3, final int n4, final TagClickEvent tagClickEvent) {
        spannableString.setSpan((Object)new ForegroundColorSpan(n3), n, n2, 33);
        spannableString.setSpan((Object)new StyleSpan(n4), n, n2, 33);
        if (tagClickEvent != null) {
            spannableString.setSpan((Object)new NonUnderlineClickableSpan() {
                @Override
                public void onClick(final View view) {
                    tagClickEvent.onTagClick(spannableString.subSequence(n, n2).toString());
                }
            }, n, n2, 33);
        }
    }

    private static void applyStyle(final SpannableString spannableString, final long n, final int n2, final int n3, final int n4, final int n5, final MentionUserClickEvent mentionUserClickEvent) {
        spannableString.setSpan((Object)new ForegroundColorSpan(n4), n2, n3, 33);
        spannableString.setSpan((Object)new StyleSpan(n5), n2, n3, 33);
        if (mentionUserClickEvent != null) {
            spannableString.setSpan((Object)new NonUnderlineClickableSpan() {
                @Override
                public void onClick(final View view) {
                    mentionUserClickEvent.onMentionUserClick(n, spannableString.subSequence(n2 + 1, n3).toString());
                }
            }, n2, n3, 33);
        }
    }

    public static SpannableString boldMention(final Editable editable, final int n, final int n2) {
        final SpannableString spannableString = new SpannableString((CharSequence)editable);
        setBold(spannableString, n, n2);
        return spannableString;
    }

    public static SpannableString boldMention(final String s) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        final Matcher matcher = Pattern.compile("@[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{1,15}").matcher(s);
        while (matcher.find()) {
            setBold(spannableString, matcher.start(), matcher.end());
        }
        return spannableString;
    }

    public static SpannableString boldString(final String s, final String s2) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        final Matcher matcher = Pattern.compile("@" + s2).matcher((CharSequence)spannableString);
        while (matcher.find()) {
            setBold(spannableString, matcher.start(), matcher.end());
        }
        return spannableString;
    }

    public static SpannableString boldString(final String s, final JSONObject jsonObject) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        try {
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String string = jsonObject.getString((String)keys.next());
                if (string != null) {
                    final String string2 = "@" + string;
                    final Matcher matcher = Pattern.compile("@" + string).matcher(s);
                    while (matcher.find()) {
                        final int start = matcher.start();
                        final int end = matcher.end();
                        s.replace(s.substring(start, end), string2);
                        setBold(spannableString, start, end);
                    }
                }
            }
        }
        catch (JSONException ex) {}
        return spannableString;
    }

    public static boolean checkInvalidNickname(final String s) {
        return Pattern.compile("[^a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]").matcher(s).find();
    }

    public static String cutString(final String s, final int n) {
        String string = s;
        if (s.length() > n) {
            string = s.substring(0, n).trim() + "...";
        }
        return string;
    }

    public static int getOverSizeByEmoji(final String s, final int n) {
        int n2 = 0;
        int n3 = 0;
        for (int n4 = 0; n4 < s.length() && n2 != n; ++n4) {
            final int charCount = Character.charCount(s.codePointAt(n4));
            if (charCount > 1) {
                n3 += charCount - 1;
            }
            else {
                ++n2;
            }
        }
        return n3;
    }

    public static int getTextIndexByScreenWidth(final TextView textView, int overSizeByEmoji, final String s) {
        if (overSizeByEmoji != 0) {
            final int breakText = textView.getPaint().breakText(s, true, (float)overSizeByEmoji, (float[])null);
            final int n = overSizeByEmoji = 0;
            if (Build.VERSION.SDK_INT < 21) {
                overSizeByEmoji = n;
                if (isStringContainsEmoji(s)) {
                    overSizeByEmoji = getOverSizeByEmoji(s, breakText);
                }
            }
            if (s.length() > breakText + overSizeByEmoji) {
                return breakText + overSizeByEmoji;
            }
        }
        return 0;
    }

    public static boolean isOverScreenWidth(final TextView textView, int breakText, final String s) {
        if (breakText != 0) {
            breakText = textView.getPaint().breakText(s, true, (float)breakText, (float[])null);
            if (s.length() > breakText) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStringContainsEmoji(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            final int codePoint = s.codePointAt(i);
            if ((codePoint >= 127744 && codePoint <= 128511) || (codePoint >= 128512 && codePoint <= 128591) || (codePoint >= 128640 && codePoint <= 128767) || (codePoint >= 9728 && codePoint <= 9983) || (codePoint >= 9986 && codePoint <= 10160) || (codePoint >= 127344 && codePoint <= 127569) || TextUtils.emojiCodeValueList.contains(codePoint)) {
                return true;
            }
        }
        return false;
    }

    public static String[] makeHighlightText(final String s, final String s2) {
        if (StringUtils.isBlank(s) || StringUtils.isBlank(s2)) {
            return null;
        }
        final String[] array = new String[3];
        final String lowerCase = s.toLowerCase();
        final String lowerCase2 = s2.toLowerCase();
        if (lowerCase.contains(lowerCase2)) {
            final int index = lowerCase.indexOf(lowerCase2);
            array[0] = s.substring(0, index);
            array[1] = s.substring(index, s2.length() + index);
            array[2] = s.substring(s2.length() + index);
            return array;
        }
        array[0] = s;
        array[2] = (array[1] = "");
        return array;
    }

    public static void makeHighlightedMention(final SpannableString spannableString, final JSONArray jsonArray, final int n, final int n2, final MentionUserClickEvent mentionUserClickEvent) {
        if (spannableString != null && jsonArray != null) {
            int n3 = 0;
            Label_0132_Outer:
            while (true) {
                Label_0188: {
                    while (true) {
                        Matcher matcher = null;
                        Label_0172: {
                            try {
                                if (n3 >= jsonArray.length()) {
                                    break;
                                }
                                final JSONObject jsonObject = jsonArray.getJSONObject(n3);
                                final String string = jsonObject.getString("memberNo");
                                final String string2 = jsonObject.getString("nickname");
                                if (string == null) {
                                    break Label_0188;
                                }
                                if (string2 == null) {
                                    break Label_0188;
                                }
                                if (!string.equalsIgnoreCase("null") && !string2.equalsIgnoreCase("null")) {
                                    matcher = Pattern.compile("@" + string2).matcher((CharSequence)spannableString);
                                    while (matcher.find()) {
                                        if (matcher.start() != 0) {
                                            break Label_0172;
                                        }
                                        final char char1 = ' ';
                                        if (!Character.isWhitespace(char1)) {
                                            continue Label_0132_Outer;
                                        }
                                        applyStyle(spannableString, Long.parseLong(string), matcher.start(), matcher.end(), n, n2, mentionUserClickEvent);
                                    }
                                }
                                break Label_0188;
                            }
                            catch (JSONException ex) {
                                ex.printStackTrace();
                                return;
                            }
                        }
                        final char char1 = spannableString.charAt(matcher.start() - 1);
                        continue;
                    }
                }
                ++n3;
            }
        }
    }

    public static void makeHighlightedTag(final SpannableString spannableString, final int n, final TagClickEvent tagClickEvent) {
        if (spannableString != null) {
            final Matcher matcher = Pattern.compile("#[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{1,20}").matcher((CharSequence)spannableString);
            while (matcher.find()) {
                applyStyle(spannableString, matcher.start(), matcher.end(), n, 1, tagClickEvent);
            }
        }
    }

    public static String makeLikeCount(final int n) {
        if (n > 1000000000) {
            return "" + n / 1000000000 + "g";
        }
        if (n > 1000000) {
            return "" + n / 1000000 + "m";
        }
        if (n > 1000) {
            return "" + n / 1000 + "k";
        }
        return "" + n;
    }

    public static ArrayList<String> makeMentionList(String s) {
        final ArrayList<String> list = new ArrayList<String>();
        final Matcher matcher = Pattern.compile("@[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{1,15}").matcher(s);
        while (matcher.find()) {
            char char1;
            if (matcher.start() == 0) {
                char1 = ' ';
            }
            else {
                char1 = s.charAt(matcher.start() - 1);
            }
            if (Character.isWhitespace(char1)) {
                list.add(s.subSequence(matcher.start() + 1, matcher.end()).toString());
            }
        }
        return list;
    }

    private static String replaceFirst(final String s, final String s2, final String s3, final String s4) {
        if (s4.equalsIgnoreCase("null")) {
            return s.replaceFirst(s2, '\uff20' + s3);
        }
        return s.replaceFirst(s2, "@" + s4);
    }

    public static String replaceMembernoToNickname(String s, final JSONArray jsonArray) {
        String s2 = s;
        if (s != null) {
            if (jsonArray == null) {
                s2 = s;
            }
            else {
                int n = 0;
                while (true) {
                    s2 = s;
                    String replaceAll;
                    try {
                        if (n >= jsonArray.length()) {
                            break;
                        }
                        final JSONObject jsonObject = jsonArray.getJSONObject(n);
                        final String string = jsonObject.getString("memberNo");
                        final String string2 = jsonObject.getString("nickname");
                        replaceAll = s;
                        if (string != null) {
                            if (string2 == null) {
                                replaceAll = s;
                            }
                            else {
                                replaceAll = s;
                                if (!string.equalsIgnoreCase("null")) {
                                    replaceAll = s;
                                    if (!string2.equalsIgnoreCase("null")) {
                                        replaceAll = s.replaceAll("@" + string, "@" + string2);
                                    }
                                }
                            }
                        }
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                        return s;
                    }
                    ++n;
                    s = replaceAll;
                }
            }
        }
        return s2;
    }

    public static String replaceMembernoToNickname(String replaceAll, final JSONObject jsonObject) {
        String s = replaceAll;
        String s2;
        try {
            final Iterator keys = jsonObject.keys();
            while (true) {
                s = replaceAll;
                s2 = replaceAll;
                if (!keys.hasNext()) {
                    break;
                }
                s = replaceAll;
                final String s3 = keys.next().toString();
                s = replaceAll;
                final String string = jsonObject.getString(s3);
                if (s3 == null || string == null) {
                    continue;
                }
                s = replaceAll;
                replaceAll = replaceAll.replaceAll("@" + s3, "@" + string);
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            s2 = s;
        }
        return s2;
    }

    public static String replaceNicknameToMemberno(String s, final JSONObject jsonObject) {
        String s2 = s;
        String s3;
        try {
            final Iterator keys = jsonObject.keys();
            while (true) {
                s2 = s;
                s3 = s;
                if (!keys.hasNext()) {
                    break;
                }
                s2 = s;
                final String s4 = keys.next().toString();
                s2 = s;
                final String string = jsonObject.getString(s4);
                if (string == null || s4 == null) {
                    continue;
                }
                s2 = s;
                if (s4.equalsIgnoreCase("null")) {
                    continue;
                }
                s2 = s;
                final String string2 = "@" + s4;
                s2 = s;
                final Matcher matcher = Pattern.compile(string2).matcher(s);
                String s5 = s;
                while (true) {
                    s = s5;
                    s2 = s5;
                    if (!matcher.find()) {
                        break;
                    }
                    s2 = s5;
                    if (s5.length() > matcher.end()) {
                        s2 = s5;
                        char char1;
                        if (matcher.start() == 0) {
                            char1 = ' ';
                        }
                        else {
                            s2 = s5;
                            char1 = s5.charAt(matcher.start() - 1);
                        }
                        s2 = s5;
                        final char char2 = s5.charAt(matcher.end());
                        s2 = s5;
                        if (!Character.isWhitespace(char1)) {
                            continue;
                        }
                        s2 = s5;
                        if (!Character.isWhitespace(char2)) {
                            continue;
                        }
                        s2 = s5;
                        s5 = replaceFirst(s5, string2, s4, string);
                    }
                    else {
                        s2 = s5;
                        s5 = replaceFirst(s5, string2, s4, string);
                    }
                }
            }
        }
        catch (JSONException ex) {
            s3 = s2;
        }
        return s3;
    }

    public static void setBold(final SpannableString spannableString, final int n, final int n2) {
        spannableString.setSpan((Object)new StyleSpan(1), n, n2, 33);
    }

    private static void setClickable(final SpannableString spannableString, final int n, final int n2, final long n3, final MentionUserClickEvent mentionUserClickEvent) {
        spannableString.setSpan((Object)new NonUnderlineClickableSpan() {
            @Override
            public void onClick(final View view) {
                mentionUserClickEvent.onMentionUserClick(n3, spannableString.subSequence(n + 1, n2).toString());
            }
        }, n, n2, 33);
    }

    public static SpannableString setMentionStyle(final String s, final JSONArray jsonArray, final MentionUserClickEvent mentionUserClickEvent) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
//        int n = 0;
//        Label_0143_Outer:
//        while (true) {
//            Label_0197: {
//                while (true) {
//                    int start = 0;
//                    Label_0185: {
//                        try {
//                            if (n < jsonArray.length()) {
//                                final JSONObject jsonObject = jsonArray.getJSONObject(n);
//                                final String string = jsonObject.getString("memberNo");
//                                final String string2 = jsonObject.getString("nickname");
//                                if (string == null) {
//                                    break Label_0197;
//                                }
//                                if (string2 == null) {
//                                    break Label_0197;
//                                }
//                                if (!string.equalsIgnoreCase("null") && !string2.equalsIgnoreCase("null")) {
//                                    final Matcher matcher = Pattern.compile("@" + string2).matcher(s);
//                                    while (matcher.find()) {
//                                        start = matcher.start();
//                                        final int end = matcher.end();
//                                        if (start != 0) {
//                                            break Label_0185;
//                                        }
//                                        final char char1 = ' ';
//                                        if (!Character.isWhitespace(char1)) {
//                                            continue Label_0143_Outer;
//                                        }
//                                        setBold(spannableString, start, end);
//                                        setClickable(spannableString, start, end, Long.parseLong(string), mentionUserClickEvent);
//                                    }
//                                }
//                                break Label_0197;
//                            }
//                        }
//                        catch (JSONException ex) {
//                            ex.printStackTrace();
//                        }
//                        break;
//                    }
//                    final char char1 = s.charAt(start - 1);
//                    continue;
//                }
//            }
//            ++n;
//        }
        return spannableString;
    }

    public static SpannableString setMentionStyle(final String s, final JSONObject jsonObject, final MentionUserClickEvent mentionUserClickEvent) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        try {
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s2 = keys.next().toString();
                final String string = jsonObject.getString(s2);
                if (s2 != null && string != null && !s2.equalsIgnoreCase("null") && !string.equalsIgnoreCase("null")) {
                    final Matcher matcher = Pattern.compile("@" + string).matcher(s);
                    while (matcher.find()) {
                        final int start = matcher.start();
                        final int end = matcher.end();
                        char char1;
                        if (start == 0) {
                            char1 = ' ';
                        }
                        else {
                            char1 = s.charAt(start - 1);
                        }
                        if (Character.isWhitespace(char1)) {
                            setBold(spannableString, start, end);
                            setClickable(spannableString, start, end, Long.parseLong(s2), mentionUserClickEvent);
                        }
                    }
                }
            }
        }
        catch (JSONException ex) {}
        return spannableString;
    }

    public static SpannableString setNicknameMentionStyle(final String s, final JSONObject jsonObject, final MentionUserClickEvent mentionUserClickEvent) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        try {
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s2 = keys.next().toString();
                final String string = jsonObject.getString(s2);
                if (s2 != null && string != null && !s2.equalsIgnoreCase("null") && !string.equalsIgnoreCase("null")) {
                    final Matcher matcher = Pattern.compile("@" + s2).matcher(s);
                    while (matcher.find()) {
                        final int start = matcher.start();
                        final int end = matcher.end();
                        char char1;
                        if (matcher.start() == 0) {
                            char1 = ' ';
                        }
                        else {
                            char1 = spannableString.charAt(matcher.start() - 1);
                        }
                        if (Character.isWhitespace(char1)) {
                            setBold(spannableString, start, end);
                            setClickable(spannableString, start, end, Long.parseLong(string), mentionUserClickEvent);
                        }
                    }
                }
            }
        }
        catch (JSONException ex) {}
        return spannableString;
    }

    public static void setUrlLinkable(final Context context, final TextView textView) {
        final SpannableString text = new SpannableString(textView.getText());
        if (text != null && StringUtils.isNotBlank((CharSequence)text)) {
            final Pattern compile = Pattern.compile("((https?:\\/\\/)|(www\\.)|(m\\.))([-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+)|\\w+\\.blog\\.me\\/*[\\w\\/]*");
            if (compile.matcher((CharSequence)text).find()) {
                final Matcher matcher = compile.matcher((CharSequence)text);
                for (int i = 0; i < matcher.groupCount(); ++i) {
                    while (matcher.find()) {
                        final int start = matcher.start(1);
                        final int end = matcher.end(1);
                        text.setSpan((Object)new ClickableSpan() {
                            final /* synthetic */ String val$link = text.subSequence(start, end).toString();

                            public void onClick(final View view) {
//                                NaverNoticeUtil.showLink(context, this.val$link);
                            }

                            public void updateDrawState(final TextPaint textPaint) {
                                textPaint.setColor(Color.parseColor("#384CD8"));
                                textPaint.setUnderlineText(false);
                            }
                        }, start, end, 33);
                    }
                }
                textView.setText((CharSequence)text);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    public static String unescape(String s) {
        if (s == null) {
            s = null;
        }
        else {
            String replace = s;
            if (s.indexOf("&") > -1) {
                replace = s.replace("&", "&");
            }
            s = replace;
            if (replace.indexOf("&") > -1) {
                s = replace.replace("&", "&");
            }
            String replace2 = s;
            if (s.indexOf("\"") > -1) {
                    replace2 = s.replace("\"", "\"");
        }
        s = replace2;
        if (replace2.indexOf("<") > -1) {
            s = replace2.replace("<", "<");
        }
        String replace3 = s;
        if (s.indexOf(">") > -1) {
            replace3 = s.replace(">", ">");
        }
        s = replace3;
        if (replace3.indexOf("!") > -1) {
            s = replace3.replace("!", "!");
        }
        String replace4 = s;
        if (s.indexOf("\"") > -1) {
                replace4 = s.replace("\"", "\"");
    }
    s = replace4;
    if (replace4.indexOf("#") > -1) {
    s = replace4.replace("#", "#");
}
    String replace5 = s;
    if (s.indexOf("$") > -1) {
    replace5 = s.replace("$", "$");
}
    s = replace5;
    if (replace5.indexOf("%") > -1) {
    s = replace5.replace("%", "%");
}
    String replace6 = s;
    if (s.indexOf("'") > -1) {
    replace6 = s.replace("'", "'");
}
    s = replace6;
    if (replace6.indexOf("^") > -1) {
    return replace6.replace("^", "^");
}
}
return s;
        }
        }