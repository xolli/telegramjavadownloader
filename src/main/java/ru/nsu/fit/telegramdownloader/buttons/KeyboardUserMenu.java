package ru.nsu.fit.telegramdownloader.buttons;

public class KeyboardUserMenu extends Keyboard {
    final static int CNT_OF_ROWS = 3;
    final static int ROW_WITH_STATS = 0;
    final static int ROW_WITH_HELP = 1;
    final static int ROW_WITH_STOP_ALL = 2;
    public final static String STATS = "Check statistics";
    public final static String HELP = "Help";
    public final static String STOP_ALL = "Stop all downloading";

    public KeyboardUserMenu() {
        super(CNT_OF_ROWS);
        initButtons();
        setKeyBoard();
    }
    public KeyboardUserMenu(int cntOfRows) {
        super(cntOfRows);
        initButtons();
    }

    private void initButtons() {
        setButtonsInRow(rowList.get(ROW_WITH_STATS),STATS);
        setButtonsInRow(rowList.get(ROW_WITH_HELP),HELP);
        setButtonsInRow(rowList.get(ROW_WITH_STOP_ALL), STOP_ALL);
    }
}
