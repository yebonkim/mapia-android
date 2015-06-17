package com.mapia.api;

/**
 * Created by daehyun on 15. 6. 13..
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.crypto.Mac;


public enum Type
{
    FILE {
        @Override
        public Mac getMac(final String s) throws Exception {
            final Properties properties = new Properties();
            InputStream inputStream = null;
            InputStream resourceAsStream;
            try {
                resourceAsStream = MACManager.class.getResourceAsStream(s);
                if (resourceAsStream == null) {
                    inputStream = resourceAsStream;
                    throw new FileNotFoundException(s);
                }
            }
            finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            properties.load(resourceAsStream);
            final Mac mac = HmacUtil.getMac((properties).elements().nextElement().toString());
            if (resourceAsStream != null) {
                resourceAsStream.close();
            }
            return mac;
        }
    },
    KEY {
        @Override
        public Mac getMac(final String s) throws Exception {
            return HmacUtil.getMac(s);
        }
    };

    public abstract Mac getMac(final String p0) throws Exception;
}
