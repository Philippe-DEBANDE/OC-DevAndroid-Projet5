package com.cleanup.todoc;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {


    // FOR DATA
    private TodocDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    // DATA SET FOR TEST
    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "test", 0xFFEADAD1);
    private static long TASK_ID = 1;
    // Change the TimeStamp and The Color
    private static Task TASK_DEMO = new Task(TASK_ID,1,"Laver Vitres", 1583658000);

    @Test
    public void getTaskWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<Task> taskList = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void insertAndGetTask() throws InterruptedException {
        // BEFORE : Adding a new task
        this.database.projectDao().insertProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO);
        // TEST
        Task task = LiveDataTestUtil.getValue(this.database.taskDao().getTask(TASK_ID));
        assertTrue(task.getName().equals(TASK_DEMO.getName())
                && task.getId() == TASK_ID
                && task.getProjectId() == PROJECT_ID
                && task.getCreationTimestamp() == TASK_DEMO.getCreationTimestamp());
    }

    @Test
    public void insertAndUpdateTask() throws InterruptedException {
        // BEFORE : Adding demo  project & task items. Next, update item added & re-save it
        this.database.projectDao().insertProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        taskAdded.setName("Laver toutes les vitres");
        this.database.taskDao().updateTask(taskAdded);

        //TEST
        List<Task> taskList = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(taskList.size() == 1 && taskList.get(0).getName().equalsIgnoreCase("Laver toutes les vitres"));
    }


    @Test
    public void insertAndDeleteItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
        this.database.projectDao().insertProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().deleteTask(taskAdded.getId());

        //TEST
        List<Task> taskList = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(taskList.isEmpty());
    }


    @After
    public void closeDb() throws Exception {
        database.close();
    }
}
