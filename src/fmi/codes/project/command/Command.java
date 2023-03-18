package fmi.codes.project.command;

import java.util.List;

public record Command(String username, CommandType commandType, List<String> arguments) {
}
