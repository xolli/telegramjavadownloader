package ru.nsu.fit.telegramdownloader.buttons;

public class KeyboardAdminMenu extends KeyboardUserMenu {
    final static int CNT_OF_ROWS = 2 + KeyboardUserMenu.CNT_OF_ROWS;
    final static int ROW_WITH_ALL_STATS = KeyboardUserMenu.CNT_OF_ROWS;
    final static int ROW_WITH_GENERATE_TOKEN = KeyboardUserMenu.CNT_OF_ROWS + 1;
    public final static String ALL_STATS = "Check All statistics";
    public final static String GENERATE_TOKEN = "Generate token";
    public KeyboardAdminMenu(){
        super(CNT_OF_ROWS);
        setButtonsInRow(rowList.get(ROW_WITH_ALL_STATS), ALL_STATS);
        setButtonsInRow(rowList.get(ROW_WITH_GENERATE_TOKEN), GENERATE_TOKEN);
        setKeyBoard();
    }
    public KeyboardAdminMenu(int cntOfRows){
        super(cntOfRows);
        setButtonsInRow(rowList.get(ROW_WITH_ALL_STATS), ALL_STATS);
        setButtonsInRow(rowList.get(ROW_WITH_GENERATE_TOKEN), GENERATE_TOKEN);
    }
}
