package one.opensource.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

public class ChineseNameUtil {

    private static Random random = new Random(new Date().getTime());

    private static final String[] FIRST_NAMES = new String[]{"李", "王", "张",
            "刘", "陈", "杨", "黄", "赵", "周", "吴", "徐", "孙", "朱", "马", "胡", "郭", "林",
            "何", "高", "梁", "郑", "罗", "宋", "谢", "唐", "韩", "曹", "许", "邓", "萧", "冯",
            "曾", "程", "蔡", "彭", "潘", "袁", "於", "董", "余", "苏", "叶", "吕", "魏", "蒋",
            "田", "杜", "丁", "沈", "姜", "范", "江", "傅", "钟", "卢", "汪", "戴", "崔", "任",
            "陆", "廖", "姚", "方", "金", "邱", "夏", "谭", "韦", "贾", "邹", "石", "熊", "孟",
            "秦", "阎", "薛", "侯", "雷", "白", "龙", "段", "郝", "孔", "邵", "史", "毛", "常",
            "万", "顾", "赖", "武", "康", "贺", "严", "尹", "钱", "施", "牛", "洪", "龚", "东方",
            "夏侯", "诸葛", "尉迟", "皇甫", "宇文", "鲜于", "西门", "司马", "独孤", "公孙", "慕容", "轩辕",
            "左丘", "欧阳", "皇甫", "上官", "闾丘", "令狐"};
    /*
     * "欧阳",
     * "太史", "端木", "上官", "司马", "东方", "独孤", "南宫", "万俟", "闻人", "夏侯", "诸葛", "尉迟",
     * "公羊", "赫连", "澹台", "皇甫", "宗政", "濮阳", "公冶", "太叔", "申屠", "公孙", "慕容", "仲孙",
     * "钟离", "长孙", "宇文", "司徒", "鲜于", "司空", "闾丘", "子车", "亓官", "司寇", "巫马", "公西",
     * "颛孙", "壤驷", "公良", "漆雕", "乐正", "宰父", "谷梁", "拓跋", "夹谷", "轩辕", "令狐", "段干",
     * "百里", "呼延", "东郭", "南门", "羊舌", "微生", "公户", "公玉", "公仪", "梁丘", "公仲", "公上",
     * "公门", "公山", "公坚", "左丘", "公伯", "西门", "公祖", "第五", "公乘", "贯丘", "公皙", "南荣",
     * "东里", "东宫", "仲长", "子书", "子桑", "即墨", "达奚", "褚师", "吴铭"
     */

    public static String generate() {
        return genFirstName() + genRandomLengthChineseChars(1, 2);
    }

    private static String genRandomLengthChineseChars(int start, int end) {
        String str = "";
        int length = random.nextInt(end + 1);
        if (length < start) {
            str = genRandomLengthChineseChars(start, end);
        } else {
            for (int i = 0; i < length; i++) {
                str = str + genOneChineseChars();
            }
        }
        return str;
    }

    private static String genOneChineseChars() {
        String str = null;
        int highPos = (176 + Math.abs(random.nextInt(39)));
        int lowPos = 161 + Math.abs(random.nextInt(93));
        byte[] b = new byte[]{(new Integer(highPos)).byteValue(),
                (new Integer(lowPos)).byteValue()};
        try {
            str = new String(b, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String genFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }

}
