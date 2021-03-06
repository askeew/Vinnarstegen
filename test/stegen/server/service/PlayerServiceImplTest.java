package stegen.server.service;

import static org.junit.Assert.*;

import org.junit.*;

import stegen.shared.*;

import com.google.appengine.tools.development.testing.*;

public class PlayerServiceImplTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalTaskQueueTestConfig(),
			new LocalDatastoreServiceTestConfig());
	private PlayerServiceImpl playerServiceImpl;

	@Before
	public void setUp() {
		helper.setUp();
		playerServiceImpl = new PlayerServiceImpl();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testChangeNicknameToLooongName() {
		LoginDataDto loginData = LoginDataDtoFactory.createLoginData();
		playerServiceImpl.registerPlayer(loginData.player.email);
		String nickname = playerServiceImpl.getNickname(loginData.player.email);
		assertEquals("address", nickname);

		String newNickname = playerServiceImpl.changeNickname(loginData.player, "tomatochgurkaiensallad");
		assertEquals("tomatochgurkaiensall", newNickname);
	}
	
	@Test
	public void testCorrectNewUserPasswordOk(){
		final String newUserPasswordNewUserPassword = "Waldner";
		assertTrue("password incorrect: "+newUserPasswordNewUserPassword, playerServiceImpl.isNewUserPasswordOk(newUserPasswordNewUserPassword));
	}
	
	@Test
	public void testInCorrectNewUserPasswordOk(){
		final String newUserPassword = "waldner";
		assertFalse("password incorrect: "+newUserPassword, playerServiceImpl.isNewUserPasswordOk(newUserPassword));
	}

}
