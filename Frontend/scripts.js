document.addEventListener("DOMContentLoaded", function () {
    const gameBoard = document.getElementById("gameBoard");
    const gameResult = document.getElementById("gameResult");
    const startButton = document.getElementById("startButton");
    const resetButton = document.getElementById("resetButton");
    const nameOrEmail = document.getElementById("nameOrEmail");
    const playerChoice = document.getElementById("playerChoice");
    const leaderboardButton = document.getElementById("leaderboardButton");
    const leaderboardTable = document.getElementById("leaderboard");
    let userId = null;
    let currentChoice = null;
    let isLeaderboardVisible = false;
    startButton.addEventListener("click", function () {
        startGame();
    });

    leaderboardButton.addEventListener("click", function () {
        toggleLeaderboard();
    });

    resetButton.addEventListener("click", function () {
        resetGame();
    });

    gameBoard.addEventListener("click", function (e) {
        if (e.target.tagName === "TD") {
            makeMove(e.target.id);
        }
    });

    function startGame() {
        const url = "http://localhost:8080/api/start";
        const params = new URLSearchParams({
            name: nameOrEmail.value,
            email: nameOrEmail.value,
            choice: playerChoice.value
        });

        fetch(url, {
            method: "POST",
            body: params
        })
        .then(response => response.json())
        .then(data => {
            userId = data.user.id;
            currentChoice = data.choice;
            document.getElementById("gameContainer").style.display = "block";
            resetBoard();
        })
        .catch(error => console.error("Error starting game:", error));
    }

    function makeMove(cellId) {
        const move = parseInt(cellId.replace("cell", ""));
        const url = "http://localhost:8080/api/move";
        const params = new URLSearchParams({
            move: move,
            choice: currentChoice,
            userId: userId
        });

        fetch(url, {
            method: "POST",
            body: params
        })
        .then(response => response.json())
        .then(data => {
            updateBoard(data.board);
            gameResult.textContent = data.result;

            if (data.result !== "Game Ongoing") {
                gameBoard.removeEventListener("click", makeMove);
            }
        })
        .catch(error => console.error("Error making move:", error));
    }

    function updateBoard(board) {
        for (let i = 0; i < board.length; i++) {
            const cell = document.getElementById(`cell${i}`);
            cell.textContent = board[i] ? board[i] : "";
        }
    }

    function resetBoard() {
        const cells = gameBoard.getElementsByTagName("td");
        for (let i = 0; i < cells.length; i++) {
            cells[i].textContent = "";
        }
        gameResult.textContent = "";
    }

    function resetGame() {
        const url = "http://localhost:8080/api/resetgame";

        fetch(url)
        .then(response => response.text())
        .then(message => {
            resetBoard();
            gameResult.textContent = message;
        })
        .catch(error => console.error("Error resetting game:", error));
    }

    function showLeaderboard() {
        const url = "http://localhost:8080/api/leaderboard";
    
        fetch(url)
        .then(response => response.json())
        .then(data => {
            let leaderboardTableBody = document.querySelector("#leaderboard tbody");
            leaderboardTableBody.innerHTML = ""; // Clear existing content
            
            data.players.forEach(player => {
                let row = document.createElement("tr");
                row.innerHTML = `
                    <td>${player.name}</td>
                    <td>${player.wins}</td>
                    <td>${player.losses}</td>
                    <td>${player.draws}</td>
                `;
                leaderboardTableBody.appendChild(row);
            });

            leaderboardTable.style.display = "table"; // Ensure the table is shown
        })
        .catch(error => console.error("Error fetching leaderboard:", error));
    }

    function toggleLeaderboard() {
        if (isLeaderboardVisible) {
            leaderboardTable.style.display = "none";
        } else {
            showLeaderboard();
        }
        isLeaderboardVisible = !isLeaderboardVisible; // Toggle the state
    }
});
