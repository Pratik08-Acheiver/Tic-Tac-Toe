package com.task.tictactoe.service;

import com.task.tictactoe.dao.UserRepository;
import com.task.tictactoe.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GameService {
    @Autowired
    private UserRepository userRepository;

    private char[] board;
    private boolean gameOngoing;

    public GameService() {
        resetBoard();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User findOrCreateUser(String name, String email) {
        User user = userRepository.findByNameOrEmail(name, email);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);
        }
        resetBoard();
        return user;
    }

    public String makeMove(int position, char playerSymbol) {
        if (gameOngoing && board[position] == '\0') {
            board[position] = playerSymbol;
            return checkWinner(playerSymbol);
        }
        return "invalid";
    }

    public String aiMove(char aiSymbol) {
        if (gameOngoing) {
            int position = findBestMove();
            board[position] = aiSymbol;
            return checkWinner(aiSymbol);
        }
        return "invalid";
    }

    private int findBestMove() {
        List<Integer> availablePositions = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '\0') {
                availablePositions.add(i);
            }
        }
        return availablePositions.get(new Random().nextInt(availablePositions.size()));
    }

    private String checkWinner(char playerSymbol) {
        int[][] winningPatterns = {

                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6} // Diagonals
        };

        for (int[] pattern : winningPatterns) {
            if (board[pattern[0]] == playerSymbol &&
                    board[pattern[1]] == playerSymbol &&
                    board[pattern[2]] == playerSymbol) {
                gameOngoing = false;
                return "win";
            }
        }

        for (char c : board) {
            if (c == '\0') {
                return "ongoing";
            }
        }

        gameOngoing = false;
        return "draw";
    }

    public void recordGameResult(User user, String result) {
        switch (result) {
            case "win":
                user.setWins(user.getWins() + 1);
                break;
            case "loss":
                user.setLosses(user.getLosses() + 1);
                break;
            case "draw":
                user.setDraws(user.getDraws() + 1);
                break;
        }
        userRepository.save(user);
    }

    public List<User> getTopPlayers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "wins"))
                .stream().limit(10).collect(Collectors.toList()); //using stream API to sort accordingly.
    }

    public char[] getBoard() {
        for(int i=0;i<9;i++){
            System.out.println(i+", "+ board[i]);
        }
        return board.clone(); // Returning a copy of the board.
    }

    public void resetBoard() {
        board = new char[9];
        gameOngoing = true;
    }
}



