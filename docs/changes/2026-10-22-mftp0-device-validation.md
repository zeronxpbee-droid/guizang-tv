# MiTV-MFTP0 原功能实机适配与验证

- Change-Id: `mitv-ui-20261022-mftp0-device-validation`
- 执行者：Codex。
- 授权：用户要求把原包可实现的 HDMI、信号源、Codex 和开机启动功能结合其电视实际设备完成兼容修改与验证。
- 基线：兼容分支 `78e9ba76cab7aa48d46687612df7bd556adc836f`。

## 实机发现

目标设备为 `MiTV-MFTP0` / `alita`、Android 9 / API 28、固件 `23.2.10.6129`。修改前
`start_3rd_app` 为空，`boot_auto_play=0`，系统启动计数为 71；适配包尚未安装。

电视输入框架报告 HDMI 1–4 硬件，但小米信号源面板实际只提供 HDMI 1、HDMI 2、HDMI 3、TV、AV
和 DTMB。实机调用确认 `input=23/24/25` 分别创建 `HW5/HW6/HW7` 会话，即 HDMI 1/2/3；
`input=26` 不创建会话，因此不把硬件枚举中的第四路暴露给用户。HDMI 1 检测到 Fire TV Stick，
TV Input 状态和 CEC 活动路由均指向端口 1。

此固件忽略显式指定 `SettingsBroadcastReceiver` 的信号源广播，但接受限定到
`com.xiaomi.mitv.settings` 包、包含后台接收标志的隐式广播。后者已实际显示“切换信号源”系统面板。
小米原桌面真实 Activity 受 `HOME_STATE` 签名权限保护；公开 `com.mitv.tvhome.MainActivity` 别名可用，
因此适配改回公开别名，不直接启动受保护 Activity。

## 本轮修改

- `MiTV-MFTP0` 默认显示 Fire TV、HDMI 2、HDMI 3、哔哩哔哩、小米投屏、相册和小米原桌面。
- HDMI 快捷入口只允许该机型已验证的 1–3 端口，保持 `MiTV-MTSU0` 原有 1–2 端口映射不变。
- `MiTV-MFTP0` 信号源入口使用已验证的包限定隐式广播；`MiTV-MTSU0` 保留原显式接收器路径。
- 为该机型启用小米设置、公开原桌面别名和开机启动判断，但 Codex 与开机自动进入仍保持默认关闭。

## 当前验证状态

代码构建、安装后的应用路径、Codex 两轮文字对话和完整重启仍待后续步骤验证。本记录不会把系统组件
直调结果冒充适配应用已经通过，也不会把未接设备端口的“无信号”状态描述为视频/音频通过。

首次 CI 运行 `39755127080` 在 Java 编译阶段失败：SDK stub 不公开系统使用的
`FLAG_RECEIVER_INCLUDE_BACKGROUND` 符号。实机已经证明该 flag 的行为，修复改为具名本地常量
`0x01000000`；失败构建没有产出或安装 APK，后续必须重新完整构建验证。
