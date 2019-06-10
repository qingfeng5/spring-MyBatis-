import org.fkjava.dto.support.DTOHelper;

public class TestMain {
    public static void main(String[] args) throws Exception {
        /**
         * 调用jar代码生成实体类等一系列代码，
         * 通过链家数据库
         * 第一步导入jar包org.fkjava.dto-2.0.RELEASE
         * 第二步导入dto.properties
         * 第三步创建testmain生成代码
         * 第四步，把external lib中org.fkjava.dto-2.0.RELEASE包下
         *         test中 DTOHelper dto = new DTOHelper();代码考过来
         * 第五步，提取dto.properties中数据库里面的文件，把数据库中的表解析成对应的实体类的访问对象
         */

        //就会自动创建自动生成代码对象
        DTOHelper dto = new DTOHelper();
        dto.createDto();
    }
}
