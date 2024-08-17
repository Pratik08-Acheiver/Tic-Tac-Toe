package com.task.tictactoe.controller;




import com.task.tictactoe.entities.User;
import com.task.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to Tic-Tac-Toe!");
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startGame(@RequestParam String name, @RequestParam String email, @RequestParam String choice) {
        User user = gameService.findOrCreateUser(name, email);
        Map<String, Object> response = Map.of(
                "user", user,
                "choice", choice
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/move")
    public ResponseEntity<Map<String, Object>> playerMove(@RequestParam int move, @RequestParam String choice, @RequestParam Long userId) {
        User user = gameService.findUserById(userId);
        char playerSymbol = choice.charAt(0);
        char aiSymbol = playerSymbol == 'X' ? 'O' : 'X';

        // Player move
        String result = gameService.makeMove(move, playerSymbol);
        String message;

        if (result.equals("win")) {
            gameService.recordGameResult(user, "win");
            message = "You Win!";
        } else if (result.equals("ongoing")) {
            // AI move
            result = gameService.aiMove(aiSymbol);
            if (result.equals("win")) {
                gameService.recordGameResult(user, "loss");
                message = "AI Wins!";
            } else if (result.equals("draw")) {
                gameService.recordGameResult(user, "draw");
                message = "It's a Draw!";
            } else {
                message = "Game Ongoing";
            }
        } else {
            gameService.recordGameResult(user, "draw");
            message = "It's a Draw!";
        }
        char[] board = gameService.getBoard();
        Map<String, Object> response = Map.of(
                "user", user,
                "result", message,
                "board",board
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<Map<String, Object>> leaderboard() {
        Map<String, Object> response = Map.of(
                "players", gameService.getTopPlayers()
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/resetgame")
    public ResponseEntity<String> resetGame(){
        gameService.resetBoard();
        return new ResponseEntity<>("Game is reset",HttpStatus.OK);
    }
}
