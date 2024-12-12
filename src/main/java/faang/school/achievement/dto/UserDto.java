package faang.school.achievement.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String aboutMe;
    private String phone;
    private Integer experience;
    private String city;
    private Long telegramChatId;
    private PreferredContact preference;
}
