# Idle Slayer Auto Tap — 手機編譯版

這個專案是針對單機遊戲的本機自動化原型：
- 讀取目前螢幕
- 在設定的百分比區域尋找「橘色方框＋白色劍」特徵
- 找到後執行一次點擊
- 使用冷卻時間避免重複點擊

## 最簡單的手機編譯方法：GitHub Actions

1. 在 GitHub 建立一個新的 repository，例如 `IdleSlayerAutoTap`。
2. 把這個專案的所有檔案上傳到 repository。
3. 上傳後進入 `Actions`。
4. 選 `Build APK`。
5. 點 `Run workflow`（如果沒有自動執行）。
6. 等待工作完成。
7. 打開該次 workflow 的頁面，在 `Artifacts` 下載 `IdleSlayerAutoTap-debug`。
8. 解壓縮後得到 `app-debug.apk`，下載到 Android 手機安裝。

## 第一次使用

1. 開啟 App。
2. 按「開啟 Android 輔助功能」。
3. 找到 `Idle Slayer Auto Tap` 並啟用。
4. 回 App 按「啟用自動偵測」。
5. 開啟遊戲。
6. 若誤判或抓不到，回 App 的「偵測設定」調整 X/Y 百分比。

## 重要

Android 官方的 AccessibilityService 支援螢幕截圖與手勢，但使用者必須在系統設定中明確啟用服務。此專案是本機自動化原型，不包含網路控制其他玩家的功能。