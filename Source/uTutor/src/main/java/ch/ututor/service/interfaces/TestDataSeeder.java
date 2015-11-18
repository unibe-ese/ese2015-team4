package ch.ututor.service.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.ututor.model.Lecture;
import ch.ututor.model.TutorLecture;
import ch.ututor.model.User;
import ch.ututor.model.dao.LectureDao;
import ch.ututor.model.dao.TutorLectureDao;
import ch.ututor.model.dao.UserDao;
import ch.ututor.utils.MultipartFileMocker;

@Service
public class TestDataSeeder implements InitializingBean {
	@Autowired
	private UserDao userDao;
	@Autowired
	private LectureDao lectureDao;
	@Autowired
	private TutorLectureDao tutorLectureDao;
	
	private List<Lecture> lectures = new ArrayList<Lecture>();
	
	public void afterPropertiesSet() throws Exception {
		createLectures();
		createUsers();
	}
	
	private void createLectures(){
		String[] lectureNames = {
				"History of Magic",
				"Transfiguration",
				"Herbology",
				"Apparition",
				"Alchemy",
				"Arithmancy",
				"Divination",
				"Study of Ancient Runes"
		};
		
		for(int i=0; i<lectureNames.length; i++){
			Lecture lecture = new Lecture();
			lecture.setName(lectureNames[i]);
			Lecture existingLecture = lectureDao.findByName(lecture.getName());
			if(existingLecture == null){
				lectures.add(lectureDao.save(lecture));
			}else{
				lectures.add(existingLecture);
			}
		}
	}
	
	private void createTutorLecture(User user, Lecture lecture, float grade){
		TutorLecture tutorLecture = new TutorLecture();
		tutorLecture.setGrade(grade);
		tutorLecture.setTutor(user);
		tutorLecture.setLecture(lecture);
		TutorLecture existingTutorLecture = tutorLectureDao.findByTutorAndLecture(user, lecture);
		if(existingTutorLecture == null){
			tutorLectureDao.save(tutorLecture);
		}
	}
	
	private void createUsers(){
		User user;
		user = createUser(
				"Fred",
				"Weasley",
				"fred.weasley@hogwarts.com",
				"maraudersmap",
				"Fred Weasley (1 April, 1978 – 2 May, 1998) was a pure-bloodwizard, a son of Arthur Weasley and Molly Weasley (née Prewett), brother to Bill, Charlie, Percy, Ron, and Ginny Weasley, and twin brother to George Weasley.",
				29.9F);
		user = userDao.save(user);
		createTutorLecture(user, lectures.get(0), 4.5F);

		user = createUser(
				"George",
				"Weasley",
				"george.weasley@hogwarts.com",
				"weasleyswizardwheezes",
				"George Weasley (b. 1 April, 1978) was a pure-blood wizard, son ofArthur Weasley and Molly Weasley (née Prewett), brother of Bill, Charlie,Percy, Ron, Ginny, and twin brother of the late Fred Weasley.",
				29.9F);
		user = userDao.save(user);
		createTutorLecture(user, lectures.get(1), 5F);
		createTutorLecture(user, lectures.get(2), 5.5F);
		
		
		user = createUser(
				"Ginny",
				"Weasley",
				"ginevra.weasley@hogwarts.com",
				"h4rryisth3b3st");
		user = userDao.save(user);
		removeLectures(user);
		
		user = createUser(
				"Percy",
				"Weasley",
				"percy.weasley@hogwarts.com",
				"imsorrydad123",
				"Percy Ignatius Weasley (b. 22 August, 1976) was a pure-bloodwizard, the third child of Arthur and Molly Weasley (née Prewett).",
				29.9F);
		user = userDao.save(user);
		createTutorLecture(user, lectures.get(3), 6F);
		createTutorLecture(user, lectures.get(4), 5.5F);
		createTutorLecture(user, lectures.get(5), 5F);
		createTutorLecture(user, lectures.get(6), 4.5F);
		createTutorLecture(user, lectures.get(7), 4F);
	}
	
	private void removeLectures(User user){
		List<TutorLecture> tutorLectures = tutorLectureDao.findByTutorOrderByLectureName(user);
		for(TutorLecture tutorLecture : tutorLectures){
			tutorLectureDao.delete(tutorLecture);
		}
	}
	
	private User addImage(User user){
		try{
			MultipartFile multipartFile = MultipartFileMocker.mockJpeg("src/main/webapp/WEB-INF/data/img/" + user.getFirstName() + "_" + user.getLastName()+ ".jpg");
			user.setProfilePic(multipartFile.getBytes());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return user;
	}
	
	private User createUser(String firstName, String lastName, String username, String password){
		User user = userDao.findByUsername(username);
		if(user == null){
			user = new User();
		}
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);
		user.setPassword(password);
		user.setIsTutor(false);
		user.setDescription(null);
		user.setPrice(0);
		user = addImage(user);
		return user;
	}
	
	private User createUser(String firstName, String lastName, String username, String password, String description, Float price){
		User user = createUser(firstName, lastName, username, password);
		user.setIsTutor(true);
		user.setDescription(description);
		user.setPrice(price);
		return user;
	}
}
