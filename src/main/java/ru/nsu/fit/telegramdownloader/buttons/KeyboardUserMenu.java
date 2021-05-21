package ru.nsu.fit.telegramdownloader.buttons;

public class KeyboardUserMenu extends Keyboard {
    final static int CNT_OF_ROWS = 2;
    final static int ROW_WITH_STATS = 0;
    final static int ROW_WITH_HELP = 1;
    public final static String STATS = "Check statistics";
    public final static String HELP = "Help";
    public KeyboardUserMenu() {
        super(CNT_OF_ROWS);
        setButtonsInRow(rowList.get(ROW_WITH_STATS),STATS);
        setButtonsInRow(rowList.get(ROW_WITH_HELP),HELP);
        setKeyBoard();
    }
    public KeyboardUserMenu(int cntOfRows) {
        super(cntOfRows);
        setButtonsInRow(rowList.get(ROW_WITH_STATS),STATS);
        setButtonsInRow(rowList.get(ROW_WITH_HELP),HELP);
    }
}
