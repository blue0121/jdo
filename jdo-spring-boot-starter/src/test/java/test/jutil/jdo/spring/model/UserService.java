package test.jutil.jdo.spring.model;

import io.jutil.jdo.spring.annotation.JdoTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2022-01-18
 */
@Service
@JdoTransactional
public class UserService {
	private UserDao userDao;

	public UserService() {
	}

	public void saveList(List<UserEntity> userList) {
		if (userList == null || userList.isEmpty()) {
			return;
		}
		userDao.saveList(userList);
	}

	public int count(Map<String, Object> param) {
		return userDao.count(param);
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
