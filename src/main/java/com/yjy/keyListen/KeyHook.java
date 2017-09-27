package com.yjy.keyListen;

import com.yjy.keyListen.base.Base;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;

/** Sample implementation of a low-level keyboard hook on W32. */
class KeyHook extends Base {

    private static HHOOK hhk;

    KeyHook () {
        // 启动监听键盘线程
        final User32 lib = User32.INSTANCE;
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        LowLevelKeyboardProc keyboardHook = (nCode, wParam, info) -> {
            if (wParam.intValue() == 256) {
                DataManager.count++;
            }
            Pointer ptr = info.getPointer();
            long peer = Pointer.nativeValue(ptr);
            return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));
        };
        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);

        // This bit never returns from GetMessage
        WinUser.MSG msg = new WinUser.MSG();
        lib.GetMessage(msg, null, 0, 0);
    }


}