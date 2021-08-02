package com.shrb.hop.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成序列号,格式:[yyyyMMddHHmmss][8位序列号]
 *
 * @author duxiaoyang
 */
public class SequenceUtil {

    private int seq;
    private DecimalFormat seqFormat = null;
    private SimpleDateFormat dateFormat = null;

    private static SequenceUtil instance = null;

    private SequenceUtil() {
        seq = 1;
        seqFormat = new DecimalFormat("00000000");
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    public static synchronized SequenceUtil getInstance() {
        if (instance == null) {
            instance = new SequenceUtil();
        }
        return instance;
    }

    public String generate() {
        int curSequence;
        synchronized (SequenceUtil.class) {
            if (seq > 99999999) {
                seq = 1;
            }
            curSequence = seq++;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dateFormat.format(new Date()));
        sb.append(seqFormat.format(curSequence));
        return sb.toString();
    }

}
