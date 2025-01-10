package faang.school.achievement.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String aboutMe;
    private String phone;
    private String countryTitle;
    private Integer experience;
    private String city;
    private Long telegramChatId;
    private PreferredContact preference;
    private String locale;
}
