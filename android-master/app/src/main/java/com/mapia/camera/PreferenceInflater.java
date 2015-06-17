package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public class PreferenceInflater {
    private static final Class[] CTOR_SIGNATURE;
    private static final String PACKAGE_NAME;
    private static final HashMap<String, Constructor> sConstructorMap;
    private Context mContext;

    static {
        PACKAGE_NAME = PreferenceInflater.class.getPackage().getName();
        CTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class};
        sConstructorMap = new HashMap<String, Constructor>();
    }

    public PreferenceInflater(final Context mContext) {
        super();
        this.mContext = mContext;
    }

    private CameraPreference inflate(final XmlPullParser xmlPullParser) {
        ArrayList<PreferenceGroup> list;
        while (true) {
            final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            list = new ArrayList<PreferenceGroup>();
            final Context mContext = this.mContext;

            int n = 0;
            try {
                n = xmlPullParser.next();

                final CameraPreference preference = this.newPreference(xmlPullParser.getName(), new Object[]{mContext, attributeSet});
                final int depth = xmlPullParser.getDepth();
                // iftrue(Label_0123:, depth <= list.size())
                list.add((PreferenceGroup) preference);
                // iftrue(Label_0030:, depth <= 1)
                list.get(depth - 2).addChild(preference);
                n = xmlPullParser.next();

            } catch (XmlPullParserException ex) {
                throw new InflateException((Throwable) ex);
            } catch (IOException ex2) {
                throw new InflateException(xmlPullParser.getPositionDescription(), (Throwable) ex2);
            }
            if (n == 1) {
                break;
            }
            if (n != 2) {
                continue;
            }
            break;
        }


        if (list.size() == 0) {
            throw new InflateException("No root element found");
        }
        return list.get(0);
    }

    private CameraPreference newPreference(final String s, final Object[] array) {
        final String string = PreferenceInflater.PACKAGE_NAME + "." + s;

        Constructor constructor;
        if ((constructor = PreferenceInflater.sConstructorMap.get(string)) != null) {
            return null;
        }
        try {
            constructor = this.mContext.getClassLoader().loadClass(string).getConstructor(PreferenceInflater.CTOR_SIGNATURE);
            PreferenceInflater.sConstructorMap.put(string, constructor);
            return (CameraPreference) constructor.newInstance(array);
        } catch (NoSuchMethodException ex) {
            throw new InflateException("Error inflating class " + string, (Throwable) ex);
        } catch (ClassNotFoundException ex2) {
            throw new InflateException("No such class: " + string, (Throwable) ex2);
        } catch (Exception ex3) {
            throw new InflateException("While create instance of" + string, (Throwable) ex3);
        }
    }


    public CameraPreference inflate(final int n) {
        return this.inflate((XmlPullParser) this.mContext.getResources().getXml(n));
    }
}