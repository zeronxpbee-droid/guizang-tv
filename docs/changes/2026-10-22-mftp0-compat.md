# MiTV-MFTP0 安全兼容配置

- Change-Id: `mitv-ui-20261022-mftp0-compat`
- 执行者：Codex。
- 授权：用户同意合并安全初始化 PR，并要求从原仓库最新 `main` 开始构建适合其电视的配置。
- 基线：`312c01119a327f402e68818670a6542f1e643e56`，叠加已合并的安全初始化提交 `c4cc14d572eb8ef8328f18ca5636d2a83febd239`。

## 范围

为 `MiTV-MFTP0` 增加可单元测试的设备配置：使用实际存在的 Xiaomi Media Explorer 相册入口，保留
哔哩哔哩、小米投屏和经运行时检查的小米原桌面入口。适配包使用独立 application ID 和名称，避免与
原作者包发生签名冲突。既有 `MiTV-MTSU0` HDMI、信号源和开机策略保持原样，Codex 实验仍默认关闭。

## 安全边界

不把只读发现等同于实机功能验证。`MiTV-MFTP0` 的 HDMI 端口参数、信号源面板和开机入口未验证，
因此不会启用，也不会出现在该机型的默认列表。没有安装 APK、修改默认 Home、写电视设置、禁用系统包、
创建真实签名密钥、写入 Codex 配对信息、发布 Release 或合并兼容 PR。

## 验证

GitHub Actions 运行 `39749608158` 对提交 `45da73ecec645743812890a2d46328f0e2c98882` 完成
Debug APK 构建、Java 单元测试、Android lint 和 Python 壁纸测试，全部通过。下载的 1.9 MB 产物位于
`artifacts/run-39749608158/app-debug.apk`，SHA-256 为
`82ff1b32c0b3da12c8eee35c63e13779be2136a361106a928317d1c211000697`，ZIP 完整性检查通过。

本机系统 Python 因缺少 Pillow，在收集壁纸测试时失败；CI 使用隔离虚拟环境安装项目声明的 Pillow 后
通过同一测试。该结果不冒充本机依赖已完备。电视端功能仍需要用户另行授权安装后的分阶段实测。
