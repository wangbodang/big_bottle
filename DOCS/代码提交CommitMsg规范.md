以下是推荐的 Git 提交信息格式规范，基于业界常用的 Conventional Commits 规范，同时也适合你在团队协作或 CI/CD 流程中使用。

✅ 标准提交格式

<type>(<scope>): <subject>
<body>  // 可选
<footer> // 可选

📌 示例

feat(user): 添加用户登录功能
实现了基于JWT的用户身份认证，包含登录接口、token签发与校验。

fix(api): 修复订单查询接口返回数据错误的问题
问题原因：分页参数未正确传递到数据库查询中。
修复方式：补充参数传递并添加单元测试。

chore: 升级Spring Boot版本至2.7.18

🔠 常见的 <type> 类型
类型	含义
feat	    新功能
fix	        修复 bug
docs	    文档更新，如 README、注释等
style	    格式（不影响代码运行的改动，如空格、缩进）
refactor	重构（不影响功能的代码调整）
perf	    性能优化
test	    增加或修改测试
chore	    构建过程或辅助工具的变动，如升级依赖
revert	    回滚某次提交

🔧 scope 示例（非必须，但推荐使用）
范围例子	    用于标识模块/目录
auth	    认证/权限模块
user	    用户模块
order	    订单模块
api	        接口相关
build	    构建相关，如maven配置
deps	    依赖包管理

💡 小技巧
subject 

建议使用 祈使句，如“添加功能”而不是“已添加功能”
总字符数控制在 50 个字符以内
body 可以详细描述做了什么、为什么做、怎么做
中文/英文都可以，但团队最好统一

如果你使用的是工具（比如 commitizen、cz-customizable）还能自动规范格式，你要不要我给你配一份配置文件？