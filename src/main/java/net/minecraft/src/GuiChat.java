package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiChat extends GuiScreen {

    public GuiChat() {
        message = "";
        updateCounter = 0;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        updateCounter++;
    }

    protected void keyTyped(char c, int i) {
        if(c == '\026')
        {
            String s;
            int j;
            s = GuiScreen.getClipboardString();
            if(s == null)
            {
                s = "";
            }
            j = 100 - message.length();
            if(j > s.length())
            {
                j = s.length();
            }
            if(j > 0)
            {
                message += s.substring(0, j);
            }
        }
        if (i == 1) {
            mc.displayGuiScreen(null);
            return;
        }
        if (i == 28) {
            String s = message.trim();
            if (s.length() > 0) {
                String s1 = message.trim();
                if (!mc.lineIsCommand(s1)) {
                    mc.thePlayer.sendChatMessage(s1);
                }
            }
            mc.displayGuiScreen(null);
            return;
        }
        if (i == 14 && message.length() > 0) {
            message = message.substring(0, message.length() - 1);
        }
        if (field_20082_i.indexOf(c) >= 0 && message.length() < 100) {
            message += c;
        }
    }

    public void drawScreen(int i, int j, float f) {
        drawRect(2, height - 14, width - 2, height - 2, 0x80000000);
        drawString(fontRenderer, (new StringBuilder()).append("> ").append(message).append((updateCounter / 6) % 2 != 0 ? "" : "_").toString(), 4, height - 12, 0xe0e0e0);
        super.drawScreen(i, j, f);
    }

    protected void mouseClicked(int i, int j, int k) {
        if (k != 0) {
            return;
        }
        if (mc.inGameGUI.field_933_a == null) {
            super.mouseClicked(i, j, k);
            return;
        }
        if (message.length() > 0 && !message.endsWith(" ")) {
            message += " ";
        }
        message += mc.inGameGUI.field_933_a;
        byte byte0 = 100;
        if (message.length() > byte0) {
            message = message.substring(0, byte0);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    protected String message;
    private int updateCounter;
    private static final String field_20082_i;

    static {
        field_20082_i = ChatAllowedCharacters.allowedCharacters;
    }
}
