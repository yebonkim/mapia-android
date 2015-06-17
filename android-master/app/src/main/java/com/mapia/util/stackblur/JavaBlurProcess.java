package com.mapia.util.stackblur;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;



public class JavaBlurProcess implements BlurProcess
{
    private static final short[] stackblur_mul;
    private static final byte[] stackblur_shr;

    static {
        stackblur_mul = new short[] { 512, 512, 456, 512, 328, 456, 335, 512, 405, 328, 271, 456, 388, 335, 292, 512, 454, 405, 364, 328, 298, 271, 496, 456, 420, 388, 360, 335, 312, 292, 273, 512, 482, 454, 428, 405, 383, 364, 345, 328, 312, 298, 284, 271, 259, 496, 475, 456, 437, 420, 404, 388, 374, 360, 347, 335, 323, 312, 302, 292, 282, 273, 265, 512, 497, 482, 468, 454, 441, 428, 417, 405, 394, 383, 373, 364, 354, 345, 337, 328, 320, 312, 305, 298, 291, 284, 278, 271, 265, 259, 507, 496, 485, 475, 465, 456, 446, 437, 428, 420, 412, 404, 396, 388, 381, 374, 367, 360, 354, 347, 341, 335, 329, 323, 318, 312, 307, 302, 297, 292, 287, 282, 278, 273, 269, 265, 261, 512, 505, 497, 489, 482, 475, 468, 461, 454, 447, 441, 435, 428, 422, 417, 411, 405, 399, 394, 389, 383, 378, 373, 368, 364, 359, 354, 350, 345, 341, 337, 332, 328, 324, 320, 316, 312, 309, 305, 301, 298, 294, 291, 287, 284, 281, 278, 274, 271, 268, 265, 262, 259, 257, 507, 501, 496, 491, 485, 480, 475, 470, 465, 460, 456, 451, 446, 442, 437, 433, 428, 424, 420, 416, 412, 408, 404, 400, 396, 392, 388, 385, 381, 377, 374, 370, 367, 363, 360, 357, 354, 350, 347, 344, 341, 338, 335, 332, 329, 326, 323, 320, 318, 315, 312, 310, 307, 304, 302, 299, 297, 294, 292, 289, 287, 285, 282, 280, 278, 275, 273, 271, 269, 267, 265, 263, 261, 259 };
        stackblur_shr = new byte[] { 9, 11, 12, 13, 13, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24 };
    }

    private static void blurIteration(final int[] array, final int n, int i, final int n2, int j, int k, int l) {
        final int n3 = n - 1;
        final int n4 = i - 1;
        final int n5 = n2 * 2 + 1;
        final short n6 = JavaBlurProcess.stackblur_mul[n2];
        final byte b = JavaBlurProcess.stackblur_shr[n2];
        final int[] array2 = new int[n5];
        if (l == 1) {
            l = k * i / j;
            int n7;
            long n8;
            long n9;
            long n10;
            long n11;
            long n12;
            long n13;
            long n14;
            long n15;
            long n16;
            long n17;
            long n18;
            int n19;
            int n20;
            int n21;
            int n22;
            int n23;
            long n24;
            long n25;
            long n26;
            int n27;
            int n28;
            long n29;
            long n30;
            long n31;
            for (n7 = (k + 1) * i / j, j = l; j < n7; ++j) {
                n8 = 0L;
                n9 = 0L;
                n10 = 0L;
                n11 = 0L;
                n12 = 0L;
                n13 = 0L;
                n14 = 0L;
                n15 = 0L;
                n16 = 0L;
                k = n * j;
                for (i = 0; i <= n2; ++i) {
                    array2[i] = array[k];
                    n16 += (array[k] >>> 16 & 0xFF) * (i + 1);
                    n15 += (array[k] >>> 8 & 0xFF) * (i + 1);
                    n14 += (array[k] & 0xFF) * (i + 1);
                    n10 += (array[k] >>> 16 & 0xFF);
                    n9 += (array[k] >>> 8 & 0xFF);
                    n8 += (array[k] & 0xFF);
                }
                i = 1;
                n17 = n16;
                n18 = n11;
                while (i <= n2) {
                    l = k;
                    if (i <= n3) {
                        l = k + 1;
                    }
                    array2[i + n2] = array[l];
                    n17 += (array[l] >>> 16 & 0xFF) * (n2 + 1 - i);
                    n15 += (array[l] >>> 8 & 0xFF) * (n2 + 1 - i);
                    n14 += (array[l] & 0xFF) * (n2 + 1 - i);
                    n13 += (array[l] >>> 16 & 0xFF);
                    n12 += (array[l] >>> 8 & 0xFF);
                    n18 += (array[l] & 0xFF);
                    ++i;
                    k = l;
                }
                l = n2;
                k = n2;
                if ((i = k) > n3) {
                    i = n3;
                }
                n19 = i + j * n;
                n20 = j * n;
                for (k = 0; k < n; ++k, n20 = n21, l = i, n19 = n27, i = n28) {
                    array[n20] = (int)((array[n20] & 0xFF000000) | (n6 * n17 >>> b & 0xFFL) << 16 | (n6 * n15 >>> b & 0xFFL) << 8 | (n6 * n14 >>> b & 0xFFL));
                    n21 = n20 + 1;
                    n22 = l + n5 - n2;
                    if ((n23 = n22) >= n5) {
                        n23 = n22 - n5;
                    }
                    n24 = (array2[n23] >>> 16 & 0xFF);
                    n25 = (array2[n23] >>> 8 & 0xFF);
                    n26 = (array2[n23] & 0xFF);
                    n27 = n19;
                    if ((n28 = i) < n3) {
                        n27 = n19 + 1;
                        n28 = i + 1;
                    }
                    array2[n23] = array[n27];
                    n29 = n13 + (array[n27] >>> 16 & 0xFF);
                    n30 = n12 + (array[n27] >>> 8 & 0xFF);
                    n31 = n18 + (array[n27] & 0xFF);
                    n17 = n17 - n10 + n29;
                    n15 = n15 - n9 + n30;
                    n14 = n14 - n8 + n31;
                    ++l;
                    if ((i = l) >= n5) {
                        i = 0;
                    }
                    n10 = n10 - n24 + (array2[i] >>> 16 & 0xFF);
                    n9 = n9 - n25 + (array2[i] >>> 8 & 0xFF);
                    n8 = n8 - n26 + (array2[i] & 0xFF);
                    n13 = n29 - (array2[i] >>> 16 & 0xFF);
                    n12 = n30 - (array2[i] >>> 8 & 0xFF);
                    n18 = n31 - (array2[i] & 0xFF);
                }
            }
        }
        else if (l == 2) {
            l = k * n / j;
            int n32;
            long n33;
            long n34;
            long n35;
            long n36;
            long n37;
            long n38;
            long n39;
            long n40;
            long n41;
            int n42;
            long n43;
            long n44;
            int n45;
            int n46;
            int n47;
            int n48;
            int n49;
            int n50;
            int n51;
            int n52;
            long n53;
            long n54;
            long n55;
            int n56;
            int n57;
            long n58;
            long n59;
            long n60;
            for (n32 = (k + 1) * n / j, j = l; j < n32; ++j) {
                n33 = 0L;
                n34 = 0L;
                n35 = 0L;
                n36 = 0L;
                n37 = 0L;
                n38 = 0L;
                n39 = 0L;
                n40 = 0L;
                n41 = 0L;
                k = j;
                for (l = 0; l <= n2; ++l) {
                    array2[l] = array[k];
                    n41 += (array[k] >>> 16 & 0xFF) * (l + 1);
                    n40 += (array[k] >>> 8 & 0xFF) * (l + 1);
                    n39 += (array[k] & 0xFF) * (l + 1);
                    n35 += (array[k] >>> 16 & 0xFF);
                    n34 += (array[k] >>> 8 & 0xFF);
                    n33 += (array[k] & 0xFF);
                }
                n42 = 1;
                n43 = n41;
                n44 = n36;
                l = k;
                for (k = n42; k <= n2; ++k, l = n45) {
                    n45 = l;
                    if (k <= n4) {
                        n45 = l + n;
                    }
                    array2[k + n2] = array[n45];
                    n43 += (array[n45] >>> 16 & 0xFF) * (n2 + 1 - k);
                    n40 += (array[n45] >>> 8 & 0xFF) * (n2 + 1 - k);
                    n39 += (array[n45] & 0xFF) * (n2 + 1 - k);
                    n38 += (array[n45] >>> 16 & 0xFF);
                    n37 += (array[n45] >>> 8 & 0xFF);
                    n44 += (array[n45] & 0xFF);
                }
                n46 = n2;
                l = n2;
                if ((k = l) > n4) {
                    k = n4;
                }
                n47 = j + k * n;
                n48 = j;
                l = 0;
                n49 = n48;
                while (l < i) {
                    array[n49] = (int)((array[n49] & 0xFF000000) | (n6 * n43 >>> b & 0xFFL) << 16 | (n6 * n40 >>> b & 0xFFL) << 8 | (n6 * n39 >>> b & 0xFFL));
                    n50 = n49 + n;
                    n51 = n46 + n5 - n2;
                    if ((n52 = n51) >= n5) {
                        n52 = n51 - n5;
                    }
                    n53 = (array2[n52] >>> 16 & 0xFF);
                    n54 = (array2[n52] >>> 8 & 0xFF);
                    n55 = (array2[n52] & 0xFF);
                    n56 = n47;
                    if ((n57 = k) < n4) {
                        n56 = n47 + n;
                        n57 = k + 1;
                    }
                    array2[n52] = array[n56];
                    n58 = n38 + (array[n56] >>> 16 & 0xFF);
                    n59 = n37 + (array[n56] >>> 8 & 0xFF);
                    n60 = n44 + (array[n56] & 0xFF);
                    n43 = n43 - n35 + n58;
                    n40 = n40 - n34 + n59;
                    n39 = n39 - n33 + n60;
                    if ((k = n46 + 1) >= n5) {
                        k = 0;
                    }
                    n35 = n35 - n53 + (array2[k] >>> 16 & 0xFF);
                    n34 = n34 - n54 + (array2[k] >>> 8 & 0xFF);
                    n33 = n33 - n55 + (array2[k] & 0xFF);
                    n38 = n58 - (array2[k] >>> 16 & 0xFF);
                    n37 = n59 - (array2[k] >>> 8 & 0xFF);
                    n44 = n60 - (array2[k] & 0xFF);
                    ++l;
                    n49 = n50;
                    n46 = k;
                    n47 = n56;
                    k = n57;
                }
            }
        }
    }

    @Override
    public Bitmap blur(final Bitmap bitmap, final float n) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final int[] array = new int[width * height];
        bitmap.getPixels(array, 0, width, 0, 0, width, height);
        final int executor_THREADS = StackBlurManager.EXECUTOR_THREADS;
        final ArrayList list = new ArrayList<BlurTask>(executor_THREADS);
        final ArrayList list2 = new ArrayList<BlurTask>(executor_THREADS);
        for (int i = 0; i < executor_THREADS; ++i) {
            list.add(new BlurTask(array, width, height, (int)n, executor_THREADS, i, 1));
            list2.add(new BlurTask(array, width, height, (int)n, executor_THREADS, i, 2));
        }
        try {
            StackBlurManager.EXECUTOR.invokeAll(list);
            final ExecutorService executorService = StackBlurManager.EXECUTOR;
            final ArrayList<BlurTask> list3 = (ArrayList<BlurTask>)list2;
            executorService.invokeAll(list3);
            final int[] array2 = array;
            final int n2 = width;
            final int n3 = height;
            final Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
            return Bitmap.createBitmap(array2, n2, n3, bitmapConfig);
        }
        catch (InterruptedException ex) {

        }

        try {
            final ExecutorService executorService = StackBlurManager.EXECUTOR;
            final ArrayList<BlurTask> list3 = (ArrayList<BlurTask>)list2;
            executorService.invokeAll(list3);
            final int[] array2 = array;
            final int n2 = width;
            final int n3 = height;
            final Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
            return Bitmap.createBitmap(array2, n2, n3, bitmapConfig);
        }
        catch (InterruptedException ex2) {
            return null;
        }
    }

    private static class BlurTask implements Callable<Void>
    {
        private final int _coreIndex;
        private final int _h;
        private final int _radius;
        private final int _round;
        private final int[] _src;
        private final int _totalCores;
        private final int _w;

        public BlurTask(final int[] src, final int w, final int h, final int radius, final int totalCores, final int coreIndex, final int round) {
            super();
            this._src = src;
            this._w = w;
            this._h = h;
            this._radius = radius;
            this._totalCores = totalCores;
            this._coreIndex = coreIndex;
            this._round = round;
        }

        @Override
        public Void call() throws Exception {
            blurIteration(this._src, this._w, this._h, this._radius, this._totalCores, this._coreIndex, this._round);
            return null;
        }
    }
}