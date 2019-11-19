package link.myrecipes.front.service;

import link.myrecipes.front.common.RestTemplateHelperImpl;
import link.myrecipes.front.dto.User;
import link.myrecipes.front.dto.request.UserRequest;
import link.myrecipes.front.dto.security.UserSecurity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {
    private User user;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private RestTemplateHelperImpl restTemplateHelper;

    @Before
    public void setUp() {
        this.user = User.builder()
                .id(1)
                .username("user12")
                .password("123456")
                .name("유저")
                .phone("01012345678")
                .email("user@domain.com")
                .build();
    }

    @Test
    public void When_로그인_정보_요청_Then_정상_반환() {
        //given
        UserSecurity userSecurity = UserSecurity.builder()
                .id(1)
                .username("user12")
                .password("123456")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        given(this.restTemplateHelper.getForEntity(eq(UserSecurity.class), contains("/login"))).willReturn(userSecurity);

        //when
        final UserSecurity selectedUserSecurity = this.memberService.login(userSecurity.getUsername());

        //then
        assertThat(selectedUserSecurity, instanceOf(UserSecurity.class));
        assertThat(selectedUserSecurity.getId(), is(userSecurity.getId()));
        assertThat(selectedUserSecurity.getUsername(), is(userSecurity.getUsername()));
        assertThat(selectedUserSecurity.getPassword(), is(userSecurity.getPassword()));
        assertThat(selectedUserSecurity.isAccountNonExpired(), is(userSecurity.isAccountNonExpired()));
        assertThat(selectedUserSecurity.isAccountNonLocked(), is(userSecurity.isAccountNonLocked()));
        assertThat(selectedUserSecurity.getId(), is(userSecurity.getId()));
        assertThat(selectedUserSecurity.isCredentialsNonExpired(), is(userSecurity.isCredentialsNonExpired()));
        assertThat(selectedUserSecurity.isEnabled(), is(userSecurity.isEnabled()));

    }

    @Test
    public void When_회원정보_저장_Then_정상_반환() {
        //given
        given(this.restTemplateHelper.postForEntity(eq(User.class), contains("/members"), any(UserRequest.class))).willReturn(this.user);

        //when
        final User selectedUser = this.memberService.createMember(user.toRequestDTO());

        //then
        assertThat(selectedUser, instanceOf(User.class));
        assertThat(selectedUser.getId(), is(this.user.getId()));
        assertThat(selectedUser.getUsername(), is(this.user.getUsername()));
        assertThat(selectedUser.getPassword(), is(this.user.getPassword()));
        assertThat(selectedUser.getName(), is(this.user.getName()));
        assertThat(selectedUser.getPhone(), is(this.user.getPhone()));
        assertThat(selectedUser.getEmail(), is(this.user.getEmail()));
    }
}