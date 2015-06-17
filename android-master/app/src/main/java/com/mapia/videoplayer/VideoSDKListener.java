package com.mapia.videoplayer;

/**
 * Created by daehyun on 15. 6. 16..
 */


public interface VideoSDKListener
{

    public static final class STATUS
    {

        public static final STATUS COMPLETE;
        private static final STATUS ENUM$VALUES[];
        public static final STATUS PROGRESSING;

//        public static STATUS valueOf(String s)
//        {
////            return (STATUS)Enum.valueOf(STATUS, s);
//        }

        public static STATUS[] values()
        {
            STATUS astatus[] = ENUM$VALUES;
            int i = astatus.length;
            STATUS astatus1[] = new STATUS[i];
            System.arraycopy(astatus, 0, astatus1, 0, i);
            return astatus1;
        }

        static
        {
            PROGRESSING = new STATUS("PROGRESSING", 0);
            COMPLETE = new STATUS("COMPLETE", 1);
            ENUM$VALUES = (new STATUS[] {
                    PROGRESSING, COMPLETE
            });
        }

        private STATUS(String s, int i)
        {
//            super(s, i);
        }
    }


    public static final int TYPE_BITMAP_FILTER = 3;
    public static final int TYPE_COMBINE = 0;
    public static final int TYPE_ENCODE = 1;
    public static final int TYPE_TRUNCATE = 2;

    public abstract void onError(int i, int j);

    public abstract void onProgress(int i, STATUS status, int j);
}


enum STATUS{
}