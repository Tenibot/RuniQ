package fmi.codes.project.quiz;

import java.util.TreeMap;

public record Quiz(int quizId, String quizName, TreeMap<Integer, Task> quizTasks) {
}
