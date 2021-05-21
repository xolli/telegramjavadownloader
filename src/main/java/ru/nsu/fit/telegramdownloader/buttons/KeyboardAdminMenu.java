package ru.nsu.fit.telegramdownloader.buttons;

public class KeyboardAdminMenu extends KeyboardUserMenu {
    final static int CNT_OF_ROWS = 4;
    final static int ROW_WITH_ALLSTATS = 2;
    final static int ROW_WITH_GENERATE_TOKEN = 3;
    public final static String ALL_STATS = "Check ALL statistics";
    public final static String GENERATE_TOKEN = "Generate token";
    public KeyboardAdminMenu(){
        super(CNT_OF_ROWS);
        setButtonsInRow(rowList.get(ROW_WITH_ALLSTATS),ALL_STATS);
        setButtonsInRow(rowList.get(ROW_WITH_GENERATE_TOKEN),GENERATE_TOKEN);
        setKeyBoard();
    }
    public KeyboardAdminMenu(int cntOfRows){
        super(cntOfRows);
        setButtonsInRow(rowList.get(ROW_WITH_ALLSTATS),ALL_STATS);
        setButtonsInRow(rowList.get(ROW_WITH_GENERATE_TOKEN),GENERATE_TOKEN);
    }
}
