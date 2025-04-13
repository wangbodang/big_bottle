module.exports = {
    types: [
        { value: 'feat', name: 'feat:     新功能' },
        { value: 'fix', name: 'fix:      修复Bug' },
        { value: 'docs', name: 'docs:     修改文档' },
        { value: 'style', name: 'style:    格式调整' },
        { value: 'refactor', name: 'refactor: 重构代码' },
        { value: 'test', name: 'test:     添加测试' },
        { value: 'chore', name: 'chore:    构建配置变更' },
        { value: 'revert', name: 'revert:   回滚提交' }
    ],
    scopes: [
        { name: 'user' },
        { name: 'auth' },
        { name: 'order' },
        { name: 'config' }
    ],
    messages: {
        type: "请选择提交类型:",
        scope: "请选择修改范围（可选）:",
        customScope: "请输入自定义 scope（可跳过）:",
        subject: "简要描述本次提交（必填）:",
        body: "详细描述（可选）:",
        footer: "关联的 issue 编号（可选）:",
        confirmCommit: "确认以上提交信息？"
    },
    allowCustomScopes: true
};
