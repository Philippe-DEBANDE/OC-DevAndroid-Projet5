package com.cleanup.todoc.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {



        // REPOSITORIES
        private final TaskDataRepository taskDataSource;
        private final ProjectDataRepository projectDataSource;
        private final Executor executor;

        // DATA
        @Nullable
        private LiveData<Project> currentProject;

        public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
            this.taskDataSource = taskDataSource;
            this.projectDataSource = projectDataSource;
            this.executor = executor;
        }

        private LiveData<List<Project>> projects;
        /// a revoir
        /*public void init(long projectId) {
            if (this.currentProject != null) {
                return;
            }
            currentProject = projectDataSource.getProject(projectId);
        }*/
        ///

        // -------------
        // FOR Project
        // -------------
        // A revoir
        //public LiveData<Project> getProject(long projectId) { return this.currentProject;  }
        public LiveData<List<Project>> getProjects() {
            return projects;
        }

        // -------------
        // FOR Task
        // -------------

        public LiveData<List<Task>> getTasks() {
            return taskDataSource.getTasks();
        }

        public void createTask(Task task) {
            executor.execute(() -> {
                taskDataSource.createTask(task);
            });
        }

        public void deleteTask(long taskId) {
            executor.execute(() -> {
                taskDataSource.deleteTask(taskId);
            });
        }

        public void updateTask(Task task) {
            executor.execute(() -> {
                taskDataSource.updateTask(task);
            });
        }

    public void init() {
        if (projects == null) {
            projects = projectDataSource.getProjects();
        }
    }
}
