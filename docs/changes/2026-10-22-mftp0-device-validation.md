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
`com.xiaomi.mitv.settings` 包的隐式广播。后者已实际显示“切换信号源”系统面板。
小米原桌面真实 Activity 受 `HOME_STATE` 签名权限保护；公开 `com.mitv.tvhome.MainActivity` 别名可用，
因此适配改回公开别名，不直接启动受保护 Activity。

## 本轮修改

- `MiTV-MFTP0` 默认显示 Fire TV、HDMI 2、HDMI 3、哔哩哔哩、小米投屏、相册和小米原桌面。
- HDMI 快捷入口只允许该机型已验证的 1–3 端口，保持 `MiTV-MTSU0` 原有 1–2 端口映射不变。
- `MiTV-MFTP0` 信号源入口使用已验证的包限定隐式广播；`MiTV-MTSU0` 保留原显式接收器路径。
- 为该机型启用小米设置、公开原桌面别名和开机启动判断，但 Codex 与开机自动进入仍保持默认关闭。

## 当前验证状态

最终运行 `39756452341` 已通过 Debug 编译、全部 25 项 Java 测试、Android lint、全部 13 项 Python
连接服务测试及构建产物上传。安装包 SHA-256 为
`c2430edfa9ac5ad3ddfa6b53885ad3e78a6efa3d08809260817b09ce161d4efe`，以独立包名安装，没有替换默认 Home。

从适配应用实际验证 HDMI 1/2/3 分别进入 `HW5/HW6/HW7`，信号源面板显示 HDMI 1/2/3、TV、AV、
DTMB；小米投屏、相册和小米原桌面均打开实际系统组件。Home 键返回小米原桌面。

Codex 经应用设置手动开启，在仓库外私有配对后显示“已连接”，完成两轮可见文字问答，第二轮 `2+3`
返回 `5`。电脑连接服务只绑定局域网地址，不公开原始 App Server，也没有把凭证写入仓库。

完整重启令启动编号从 71 增加到 72。系统日志确认 `BootGateActivity` 启动 `FrameHomeActivity`，应用偏好
记录 `attempted=72`、`enabled=true` 和“已在本次开机后进入桌面”。后续遥控器关机/开机没有改变启动
编号，重复入口按设计直接结束并保留小米桌面；这与完整重启自动进入是两种行为。

仍不能把底层会话建立描述为所有端口音视频质量通过；各端口音频、视频质量、延迟、长期待机、其他固件
和电脑连接服务常驻方式仍未验证。

首次 CI 运行 `39755127080` 在 Java 编译阶段失败：SDK stub 不公开 shell 测试使用的
`FLAG_RECEIVER_INCLUDE_BACKGROUND` 符号。失败构建没有产出或安装 APK。

第二次 CI 运行 `39755562286` 已编译并打包，但 25 项 Java 测试中有 1 项失败：数组防篡改测试仍把
哔哩哔哩当作首项，没有随设备默认入口改为 HDMI 1。测试期望已修正为新配置；该次运行在单元测试失败后
未执行 lint 和 Python 测试，仍不能作为通过结果。

第三次 CI 运行 `39755996810` 通过编译和全部 25 项 Java 测试，随后 Android lint 把具名数值 flag
判定为 WrongConstant。重新实机测试确认包限定广播不需要这个隐藏 flag 也能显示系统面板，因此删除 flag，
不使用 lint 抑制绕过问题。该次运行在 lint 失败后未执行 Python 测试。
