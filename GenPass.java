import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenPass {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPass = "123456";

        // 1. 验证您数据库里现在的旧哈希值 (admin用户的)
        String oldHash = "$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGqq0jd2zm6adLI.ungxuRnFoldDOu";
        boolean matches = encoder.matches(rawPass, oldHash);
        System.out.println("旧密码哈希是否匹配 123456: " + matches);

        // 2. 生成一个新的 123456 哈希值
        String newHash = encoder.encode(rawPass);
        System.out.println("--------------------------------------------------");
        System.out.println("请复制下面的新哈希值到数据库中替换 password 字段：");
        System.out.println(newHash);
        System.out.println("--------------------------------------------------");
    }
}