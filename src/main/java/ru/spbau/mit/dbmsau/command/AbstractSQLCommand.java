package ru.spbau.mit.dbmsau.command;

import ru.spbau.mit.dbmsau.command.exception.CommandExecutionException;

abstract public class AbstractSQLCommand {
    abstract public SQLCommandResult execute() throws CommandExecutionException;
}
