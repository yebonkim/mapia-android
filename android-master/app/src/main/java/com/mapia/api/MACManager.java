package com.mapia.api;

/**
 * Created by daehyun on 15. 6. 13..
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.Mac;


public class MACManager
{
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 30000;
    private static final long DEFAULT_CORRECTION_CONDITION_MILLIS = 600000L;
    private static final long DEFAULT_CORRECTION_MILLIS = 0L;
    private static final int DEFAULT_READ_TIMEOUT_MS = 30000;
    private static final String DEFAULT_REMOTE_CURRENTTIME_URL = "http://global.apis.mapsns.com/currentTime";
    private static final String KEY_FILENAME = "/NHNAPIGatewayKey.properties";
    private static final int THREAD_COUNT = 2;
    private static int connectionTimeoutMs;
    static long correctionMillis;
    static Future<Long> correctionMillisFuture;
    private static long correctionWhenConditionMs;
    static final ExecutorService executor;
    private static volatile boolean isThreadRunning;
    private static final Object lock;
    private static Mac mac;
    private static int readTimeoutMs;
    private static String remoteCurrentTimeUrl;

    static {
        lock = new Object();
        MACManager.connectionTimeoutMs = 30000;
        MACManager.readTimeoutMs = 30000;
        MACManager.correctionWhenConditionMs = 600000L;
        MACManager.remoteCurrentTimeUrl = "http://global.apis.mapsns.com/currentTime";
        MACManager.isThreadRunning = false;
        MACManager.correctionMillis = 0L;
        executor = Executors.newFixedThreadPool(2);
    }

    private static void calcuateCorrectionTime(long correctionMillis) {
        correctionMillis -= System.currentTimeMillis();
        if (correctionMillis > MACManager.correctionWhenConditionMs) {
            MACManager.correctionMillis = correctionMillis;
            return;
        }
        MACManager.correctionMillis = 0L;
    }

    private static Future<Long> calcuateCorrectionTimeOnAnotherThread() {
        MACManager.isThreadRunning = true;
        try {
            return MACManager.correctionMillisFuture = MACManager.executor.submit((Callable<Long>)new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    try {
                        final URLConnection openConnection = new URL(MACManager.remoteCurrentTimeUrl).openConnection();
                        openConnection.setConnectTimeout(MACManager.connectionTimeoutMs);
                        openConnection.setReadTimeout(MACManager.readTimeoutMs);
                        final long correctionMillis = Long.parseLong(MACManager.toStringFromInputStream(openConnection.getInputStream(), "UTF-8")) - System.currentTimeMillis();
                        if (correctionMillis > MACManager.correctionWhenConditionMs) {
                            MACManager.correctionMillis = correctionMillis;
                        }
                        else {
                            MACManager.correctionMillis = 0L;
                        }
                        final long correctionMillis2 = MACManager.correctionMillis;
                        MACManager.isThreadRunning = false;
                        return correctionMillis2;
                    }
                    finally {
                        MACManager.isThreadRunning = false;
                    }
                }
            });
        }
        catch (Exception ex) {
            MACManager.isThreadRunning = false;
            ex.printStackTrace();
            return null;
        }
    }

    public static int getConnectionTimeoutMs() {
        return MACManager.connectionTimeoutMs;
    }

    public static long getCorrectionWhenConditionMs() {
        return MACManager.correctionWhenConditionMs;
    }

    public static String getEncryptUrl(final String s) throws Exception {
        if (MACManager.mac == null) {
            initialize();
        }
        return HmacUtil.makeEncryptUrl(MACManager.mac, s, MACManager.correctionMillis);
    }

    public static String getEncryptUrl(final String s, final long n) throws Exception {
        if (MACManager.mac == null) {
            initialize();
        }
        return HmacUtil.makeEncryptUrlWithMsgpad(MACManager.mac, s, n);
    }

    public static String getEncryptUrl(final String s, final Type type, final String s2) throws Exception {
        return HmacUtil.makeEncryptUrl(type.getMac(s2), s, MACManager.correctionMillis);
    }

    public static String getEncryptUrl(final String s, final Type type, final String s2, final long n) throws Exception {
        return HmacUtil.makeEncryptUrlWithMsgpad(type.getMac(s2), s, n);
    }

    public static int getReadTimeoutMs() {
        return MACManager.readTimeoutMs;
    }

    public static String getRemoteCurrentTimeUrl() {
        return MACManager.remoteCurrentTimeUrl;
    }

    public static void initialize() throws Exception {
        initialize(Type.FILE, "/NHNAPIGatewayKey.properties");
    }

    public static void initialize(final Type type, final String s) throws Exception {
        synchronized (MACManager.lock) {
            if (MACManager.mac == null) {
                MACManager.mac = type.getMac(s);
            }
        }
    }

    public static void setConnectionTimeoutMs(final int connectionTimeoutMs) {
        MACManager.connectionTimeoutMs = connectionTimeoutMs;
    }

    public static void setCorrectionWhenConditionMs(final long correctionWhenConditionMs) {
        MACManager.correctionWhenConditionMs = correctionWhenConditionMs;
    }

    public static void setReadTimeoutMs(final int readTimeoutMs) {
        MACManager.readTimeoutMs = readTimeoutMs;
    }

    public static void setRemoteCurrentTimeUrl(final String remoteCurrentTimeUrl) {
        MACManager.remoteCurrentTimeUrl = remoteCurrentTimeUrl;
    }

    public static void syncWithServerTime(final long n) {
        calcuateCorrectionTime(n);
    }

    public static Future<Long> syncWithServerTimeByHttpAsync() {
        if (MACManager.isThreadRunning) {
            return MACManager.correctionMillisFuture;
        }
        return calcuateCorrectionTimeOnAnotherThread();
    }

    public static Future<Long> syncWithServerTimeByHttpAsync(final int connectionTimeoutMs, final int readTimeoutMs) {
        setConnectionTimeoutMs(connectionTimeoutMs);
        setReadTimeoutMs(readTimeoutMs);
        return syncWithServerTimeByHttpAsync();
    }

    public static String toStringFromInputStream(final InputStream p0, final String p1) throws IOException {
        //
        // This method could not be decompiled.
        //
        // Original Bytecode:
        //
        //     0: new             Ljava/io/InputStreamReader;
        //     3: dup
        //     4: aload_0
        //     5: aload_1
        //     6: invokespecial   java/io/InputStreamReader.:(Ljava/io/InputStream;Ljava/lang/String;)V
        //     9: astore_1
        //    10: new             Ljava/lang/StringBuffer;
        //    13: dup
        //    14: invokespecial   java/lang/StringBuffer.:()V
        //    17: astore_3
        //    18: sipush          1024
        //    21: newarray        C
        //    23: astore          4
        //    25: aload_1
        //    26: aload           4
        //    28: invokevirtual   java/io/Reader.read:([C)I
        //    31: istore_2
        //    32: iload_2
        //    33: iconst_m1
        //    34: if_icmpeq       67
        //    37: aload_3
        //    38: new             Ljava/lang/String;
        //    41: dup
        //    42: aload           4
        //    44: iconst_0
        //    45: iload_2
        //    46: invokespecial   java/lang/String.:([CII)V
        //    49: invokevirtual   java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
        //    52: pop
        //    53: goto            25
        //    56: astore_1
        //    57: aload_0
        //    58: ifnull          65
        //    61: aload_0
        //    62: invokevirtual   java/io/InputStream.close:()V
        //    65: aload_1
        //    66: athrow
        //    67: aload_3
        //    68: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
        //    71: astore_1
        //    72: aload_0
        //    73: ifnull          80
        //    76: aload_0
        //    77: invokevirtual   java/io/InputStream.close:()V
        //    80: aload_1
        //    81: areturn
        //    82: astore_0
        //    83: aload_1
        //    84: areturn
        //    85: astore_0
        //    86: goto            65
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ---------------------
        //  0      25     56     67     Any
        //  25     32     56     67     Any
        //  37     53     56     67     Any
        //  61     65     85     89     Ljava/io/IOException;
        //  67     72     56     67     Any
        //  76     80     82     85     Ljava/io/IOException;
        //
        // The error that occurred was:
        //
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0065:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Unknown Source)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(Unknown Source)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(Unknown Source)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(Unknown Source)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(Unknown Source)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(Unknown Source)
        //     at gui.Main.g(Unknown Source)
        //     at gui.Main.a(Unknown Source)
        //     at gui.Main.a(Unknown Source)
        //     at gui.Main$26.run(Unknown Source)
        //     at com.sun.javafx.application.PlatformImpl.lambda$null$164(PlatformImpl.java:292)
        //     at com.sun.javafx.application.PlatformImpl$$Lambda$48/668422520.run(Unknown Source)
        //     at java.security.AccessController.doPrivileged(Native Method)
        //     at com.sun.javafx.application.PlatformImpl.lambda$runLater$165(PlatformImpl.java:291)
        //     at com.sun.javafx.application.PlatformImpl$$Lambda$47/815033865.run(Unknown Source)
        //     at com.sun.glass.ui.InvokeLaterDispatcher$Future.run(InvokeLaterDispatcher.java:95)
        //
        //
        // This method could not be decompiled.
        //
        // Original Bytecode:
        //
        //     0: new             Ljava/io/InputStreamReader;
        //     3: dup
        //     4: aload_0
        //     5: aload_1
        //     6: invokespecial   java/io/InputStreamReader.:(Ljava/io/InputStream;Ljava/lang/String;)V
        //     9: astore_1
        //    10: new             Ljava/lang/StringBuffer;
        //    13: dup
        //    14: invokespecial   java/lang/StringBuffer.:()V
        //    17: astore_3
        //    18: sipush          1024
        //    21: newarray        C
        //    23: astore          4
        //    25: aload_1
        //    26: aload           4
        //    28: invokevirtual   java/io/Reader.read:([C)I
        //    31: istore_2
        //    32: iload_2
        //    33: iconst_m1
        //    34: if_icmpeq       67
        //    37: aload_3
        //    38: new             Ljava/lang/String;
        //    41: dup
        //    42: aload           4
        //    44: iconst_0
        //    45: iload_2
        //    46: invokespecial   java/lang/String.:([CII)V
        //    49: invokevirtual   java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
        //    52: pop
        //    53: goto            25
        //    56: astore_1
        //    57: aload_0
        //    58: ifnull          65
        //    61: aload_0
        //    62: invokevirtual   java/io/InputStream.close:()V
        //    65: aload_1
        //    66: athrow
        //    67: aload_3
        //    68: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
        //    71: astore_1
        //    72: aload_0
        //    73: ifnull          80
        //    76: aload_0
        //    77: invokevirtual   java/io/InputStream.close:()V
        //    80: aload_1
        //    81: areturn
        //    82: astore_0
        //    83: aload_1
        //    84: areturn
        //    85: astore_0
        //    86: goto            65
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ---------------------
        //  0      25     56     67     Any
        //  25     32     56     67     Any
        //  37     53     56     67     Any
        //  61     65     85     89     Ljava/io/IOException;
        //  67     72     56     67     Any
        //  76     80     82     85     Ljava/io/IOException;
        //
        // The error that occurred was:
        //
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0065:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Unknown Source)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(Unknown Source)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(Unknown Source)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(Unknown Source)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(Unknown Source)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(Unknown Source)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(Unknown Source)
        //     at gui.Main.g(Unknown Source)
        //     at gui.Main.a(Unknown Source)
        //     at gui.Main.a(Unknown Source)
        //     at gui.Main$26.run(Unknown Source)
        //     at com.sun.javafx.application.PlatformImpl.lambda$null$164(PlatformImpl.java:292)
        //     at com.sun.javafx.application.PlatformImpl$$Lambda$48/668422520.run(Unknown Source)
        //     at java.security.AccessController.doPrivileged(Native Method)
        //     at com.sun.javafx.application.PlatformImpl.lambda$runLater$165(PlatformImpl.java:291)
        //     at com.sun.javafx.application.PlatformImpl$$Lambda$47/815033865.run(Unknown Source)
        //     at com.sun.glass.ui.InvokeLaterDispatcher$Future.run(InvokeLaterDispatcher.java:95)
        //
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}