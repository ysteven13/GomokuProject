import java.util.ArrayList;

public class AlphaBetaSearch extends Gomoku{
    int maxDepth = 100;
    point bestMove;
    point[][] gameState;

    public AlphaBetaSearch(point[][] boardState) {
        gameState = boardState;
    }
    public void searchMoves() {
        bestMove = null;
        int depth = 1;
        while (depth <= maxDepth){
            if (currentPlayer == 1) {
                initialMax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else {
                initialMin(depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            depth++;
        }
    }
    public point getBestMove() {
        return bestMove;
    }
    int initialMax(int depth, int alpha, int beta) {
        if(depth == 0) return evaluate(gameState);
        ArrayList<point> freeMoves = getMoves(gameState);
        if(freeMoves.isEmpty()) {
            return evaluate(gameState);
        }
        if(bestMove != null && freeMoves.contains(bestMove)) {
            freeMoves.remove(bestMove);
            freeMoves.add(0,bestMove);
        }
        for(point p: freeMoves) {
            doMove(p,gameState);
            int value = getMin(depth-1,alpha,beta);
            undoMove(gameState);
            if(value > alpha) {
                alpha = value;
                if(alpha >=  beta) {
                    break;
                }
                bestMove = p;
            }
        }
        return alpha;
    }
    int getMax(int depth, int alpha, int beta) {
        if (depth == 0) {
            return evaluate(gameState);
        }
        ArrayList<point> moves = getMoves(gameState);
        if (moves.isEmpty()) {
            return evaluate(gameState);
        }
        for (point move : moves) {
            doMove(move,gameState);
            int value = min(depth - 1, alpha, beta);
            undoMove(gameState);
            if (value > alpha) {
                alpha = value;
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return alpha;
    }
    int initialMin(int depth, int alpha, int beta) {
        if(depth == 0) return evaluate(gameState);
        ArrayList<point> freeMoves = getMoves(gameState);
        if(freeMoves.isEmpty()) {
            return evaluate(gameState);
        }
        for(point p: freeMoves) {
            doMove(p,gameState);
            int value = getMax(depth-1,alpha,beta);
            undoMove(gameState);
            if(value < beta) {
                beta = value;
                if(beta <=  alpha) {
                    break;
                }
                bestMove = p;
            }
        }
        return beta;
    }
    int getMin(int depth, int alpha, int beta) {
        if (depth == 0) {
            return evaluate(gameState);
        }
        ArrayList<point> moves = getMoves(gameState);
        if (moves.isEmpty()) {
            return evaluate(gameState);
        }
        for (point move : moves) {
            doMove(move,gameState);
            int value = max(depth - 1, alpha, beta);
            undoMove(gameState);
            if (value < beta) {
                beta = value;
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return beta;
    }
}
