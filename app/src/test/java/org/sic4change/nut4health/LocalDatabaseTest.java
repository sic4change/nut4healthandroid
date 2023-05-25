package org.sic4change.nut4health;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.sic4change.nut4health.data.Nut4HealtDao;
import org.sic4change.nut4health.data.Nut4HealthDatabase;
import org.sic4change.nut4health.data.entities.User;

import java.io.IOException;

import androidx.test.core.app.ApplicationProvider;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;



@RunWith(AndroidJUnit4.class)
public class LocalDatabaseTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private Nut4HealtDao nut4HealtDao;
    private Nut4HealthDatabase nut4HealthDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        nut4HealthDatabase = Room.inMemoryDatabaseBuilder(context, Nut4HealthDatabase.class).allowMainThreadQueries().build();
        nut4HealtDao = nut4HealthDatabase.nut4HealtDao();
    }

    @After
    public void closeDb() throws IOException {
        nut4HealthDatabase.close();
    }

    @Test
    public void createUserAndReadIt() throws Exception {
        User user = new User("aaron.asencio.tavio@gmail.com", "aaronat");
        nut4HealtDao.deleteAllUser();
        nut4HealtDao.insert(user);
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    public void createUserAndDeleteAll() throws Exception {
        User user = new User("aaron.asencio.tavio@gmail.com", "aaronat");
        nut4HealtDao.deleteAllUser();
        nut4HealtDao.insert(user);
        nut4HealtDao.deleteAllUser();
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb, equalTo(null));
    }

    @Test
    public void deleteEmptyUser() throws Exception {
        User user = new User("anonymous@anonymous.com", "empty");
        nut4HealtDao.insert(user);
        nut4HealtDao.deleteEmptyUser();
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb, equalTo(null));
    }

    @Test
    public void updatePhotoUser() throws Exception {
        User user = new User("aaron.asencio.tavio@gmail.com", "aaronat");
        nut4HealtDao.insert(user);
        user.setPhoto("urlPhoto");
        nut4HealtDao.updatePhotoUser("urlPhoto", "aaron.asencio.tavio@gmail.com");
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb.getPhoto(), equalTo(user.getPhoto()));
        nut4HealtDao.deleteAllUser();
    }

    @Test
    public void updateNameUser() throws Exception {
        User user = new User("aaron.asencio.tavio@gmail.com", "aaronat");
        nut4HealtDao.insert(user);
        user.setName("Aaron");
        nut4HealtDao.updateNameUser("Aaron", "aaron.asencio.tavio@gmail.com");
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb.getName(), equalTo(user.getName()));
        nut4HealtDao.deleteAllUser();
    }

    @Test
    public void updateSurnameUser() throws Exception {
        User user = new User("aaron.asencio.tavio@gmail.com", "aaronat");
        nut4HealtDao.insert(user);
        user.setSurname("Asencio");
        nut4HealtDao.updateSurnameUser("Asencio", "aaron.asencio.tavio@gmail.com");
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb.getSurname(), equalTo(user.getSurname()));
        nut4HealtDao.deleteAllUser();
    }

    @Test
    public void updateCountryUser() throws Exception {
        User user = new User("aaron.asencio.tavio@gmail.com", "aaronat");
        nut4HealtDao.insert(user);
        user.setCountry("Spain");
        nut4HealtDao.updateCountryUser("Spain", "aaron.asencio.tavio@gmail.com");
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb.getCountry(), equalTo(user.getCountry()));
        nut4HealtDao.deleteAllUser();
    }

    @Test
    public void updateCountryCodeUser() throws Exception {
        User user = new User("aaron.asencio.tavio@gmail.com", "aaronat");
        nut4HealtDao.insert(user);
        user.setCountryCode("ES");
        nut4HealtDao.updateCountryCodeUser("ES", "aaron.asencio.tavio@gmail.com");
        User userDb = getValue(nut4HealtDao.getCurrentUser());
        assertThat(userDb.getCountryCode(), equalTo(user.getCountryCode()));
        nut4HealtDao.deleteAllUser();
    }

    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        return (T) data[0];
    }
}