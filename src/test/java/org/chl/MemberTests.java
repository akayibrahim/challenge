package org.chl;

import org.chl.model.FriendList;
import org.chl.model.Member;
import org.chl.repository.FriendListRepository;
import org.chl.repository.MemberRepository;
import org.chl.service.MemberService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;;

/**
 * Created by ibrahim on 12/5/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MemberService.class)
@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepo;

    @MockBean
    private FriendListRepository friendListRepository;

    private static final String name = "ibrahim";
    private static final String surname = "akay";
    private static final String email = "that_see";
    private static final String id = "1234";
    private static final String memberId = "12345678901";
    private static final String friendMemberId = "12345678902";

    @Before
    public void setup() {
        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setSurname(surname);
        member.setEmail(email);
        Mockito.when(memberRepo.findByEmail(member.getEmail())).thenReturn(member);
        Mockito.when(memberRepo.findOne(member.getId())).thenReturn(member);

        FriendList friend = new FriendList();
        friend.setMemberId(memberId);
        friend.setFriendMemberId(friendMemberId);
        friend.setFollowed(true);
        List<FriendList> friendLists = new ArrayList<>();
        friendLists.add(friend);
        Mockito.when(friendListRepository.findByMemberId(memberId)).thenReturn(friendLists);
    }

    @Test
    public void verifyAddMember() throws  Exception{
        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setSurname(surname);
        member.setEmail(email);
        memberService.addMember(member);
    }

    @Test
    public void verifyGetMemberInfo() throws  Exception{
        Member found = memberService.getMemberInfo(id);
        assertThat(found.getEmail()).isEqualTo(email);
        assertThat(found.getName()).isEqualTo(name);
        assertThat(found.getSurname()).isEqualTo(surname);
        // assertNotNull
    }

    @Test
    public void verifyGetMemberByEmail() throws  Exception{
        Member found = memberService.getMemberInfoByEmail(email);
        assertThat(found.getEmail()).isEqualTo(email);
        assertThat(found.getName()).isEqualTo(name);
        assertThat(found.getSurname()).isEqualTo(surname);
    }

    @Test
    public void verifyAddFriend() throws  Exception{
        FriendList friendList = new FriendList();
        friendList.setMemberId(memberId);
        friendList.setFriendMemberId(friendMemberId);
        friendListRepository.save(friendList);
    }

    @Test
    public void verifyGetFriendList() throws  Exception{
        Iterable<FriendList> friendLists = friendListRepository.findByMemberId(memberId);
        for (FriendList friend:friendLists) {
            assertThat(friend.getMemberId()).isEqualTo(memberId);
            assertThat(friend.getFriendMemberId()).isEqualTo(friendMemberId);
            assertThat(friend.getFollowed()).isEqualTo(true);
        }
    }
}
