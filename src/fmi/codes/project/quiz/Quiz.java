package fmi.codes.project.quiz;

import fmi.codes.project.task.Task;

import java.util.TreeMap;

public record Quiz(int quizId, String quizName, TreeMap<Integer, Task> quizTasks) {
}
