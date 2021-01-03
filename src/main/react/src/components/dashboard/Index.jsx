import React from 'react';
import {NavLink, Redirect, Route, Switch} from 'react-router-dom';
import Dashboard from './Dashboard';
import Datasource from '../datasource/Datasource';
import MoveTask from '../movetask/MoveTask';
import axios from "axios";
import cookie from 'react-cookies'
import ImgCrop from 'antd-img-crop';
import {Avatar, Button, Drawer, Form, Image, Input, Layout, Menu, message, notification, Row, Spin, Upload} from 'antd';
import {
    EditOutlined,
    LaptopOutlined,
    NotificationOutlined,
    UploadOutlined,
    UserSwitchOutlined
} from '@ant-design/icons';

const { SubMenu } = Menu;
const { Header, Content, Sider } = Layout;
const tailLayout = {
    wrapperCol: {
        offset: 5,
        span: 15,
    },
};
const layout = {
    labelCol: {
        span: 5,
    },
    wrapperCol: {
        span: 15,
    },
};
const token = cookie.load('token');
const head = { token: token }



export default class Index extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userName: '',
            iconUrl: '',
            mainDrawer: false,
            mainDrawerWidth: 250,
            messageDrawer: false,
            passwordDrawer: false,
            placement: 'right',
            loading: false,
            index: 0,
        };
    }


    componentDidMount() {
        axios.get('/user/getCurrentUser').then((response) => {
            var result = response.data.message;
            if (result == 'Success') {
                const user = response.data.data;
                this.setState({ userName: user.userName, iconUrl: user.iconUrl })
            } else {

            }
        });
    }


    logout() {
        cookie.remove('token');
        this.props.history.push('/login')
        notification['success']({
            message: '通知',
            description:
                '注销成功',
            duration: 2,
        });
    };

    onFinish = (values) => {
        axios.post('/user/modifyPassword', {
            oldPassword: values.oldPassword,
            newPassword: values.newPassword
        }).then((response) => {
            var result = response.data.message;
            if (result == 'Success') {
                this.setState({
                    passwordDrawer: false,
                    mainDrawerWidth: 250,
                });
                notification['success']({
                    message: '通知',
                    description:
                        '修改成功',
                    duration: 1,
                });
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '旧密码错误,请重新输入',
                    duration: 1,
                });
            }
        });
    }

    quit() {
        this.setState({
            passwordDrawer: false,
            mainDrawerWidth: 250,
        });
    }

    showMainDrawer = () => {
        this.setState({
            mainDrawer: true,
        });
    };

    onMainDrawerClose = () => {
        this.setState({
            mainDrawer: false,
        });
    };

    showMessageDrawer = () => {
        this.setState({
            messageDrawer: true,
            mainDrawerWidth: 500,
        });
    };
    onMessageDrawerClose = () => {
        this.setState({
            messageDrawer: false,
            mainDrawerWidth: 250,
        });
    };

    showPasswordDrawer = () => {
        this.setState({
            passwordDrawer: true,
            mainDrawerWidth: 500,
        });
    };
    onPasswordDrawerClose = () => {
        this.setState({
            passwordDrawer: false,
            mainDrawerWidth: 250,
        });
    };


    beforeUpload(file) {
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            message.error('仅支持JPG和PNG文件');
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
            message.error('图片大小不得大于2MB');
        }
        return isJpgOrPng && isLt2M;
    }


    handleChange = info => {
        if (info.file.status === 'uploading') {
            this.setState({ loading: true });
            return;
        }
        if (info.file.status === 'done') {
            const response = info.fileList[this.state.index].response;
            if (response.message === 'Success') {
                this.setState({ iconUrl: response.data });
                this.setState({ loading: false });
                message.success('修改头像成功');
            } else {
                message.error('修改头像失败');
            }
            this.setState({ index: this.state.index + 1 });
        }
    };

    render() {
        const { userName, iconUrl, placement, mainDrawer, mainDrawerWidth, loading, fileList } = this.state;
        return <div style={{ height: '100%' }}>
            <Drawer
                title={'欢迎回来，' + userName}
                placement={placement}
                closable={false}
                onClose={this.onMainDrawerClose}
                visible={mainDrawer}
                key={placement}
                width={mainDrawerWidth}
            >
                <Spin spinning={loading}>
                    <Image
                        style={{ borderRadius: '100%', width: '100%', cursor: 'pointer' }}
                        width="202px"
                        height="202px"
                        src={iconUrl}
                    />
                </Spin>
                <div style={{ textAlign: 'center', margin: '10px 0 0 0' }}>
                    <ImgCrop
                        rotate
                        modalTitle="编辑图片"
                        modalOk="确定"
                        modalCancel="取消"
                    >
                        <Upload
                            headers={head}
                            showUploadList={false}
                            action="https://api.dtrun.cn/user/icon"
                            beforeUpload={this.beforeUpload}
                            onChange={this.handleChange}
                        >
                            <Button icon={<UploadOutlined />}>更换头像</Button>

                        </Upload>
                    </ImgCrop>
                </div>
                <Menu
                    mode="inline"
                >
                    <Menu.Item key="1" onClick={this.showMessageDrawer} icon={<UserSwitchOutlined />}>
                        个人中心
                    </Menu.Item>
                    <Menu.Item key="2" onClick={this.showPasswordDrawer} icon={<EditOutlined />}>
                        修改密码</Menu.Item>
                    <Menu.Item key="3" onClick={() => this.logout()} icon={<UploadOutlined style={{ transform: 'rotate(90deg)' }} />}>
                        注销
                    </Menu.Item>
                </Menu>

                <Drawer
                    title="个人中心"
                    width={400}
                    closable={false}
                    onClose={this.onMessageDrawerClose}
                    visible={this.state.messageDrawer}
                >
                    个人中心
          </Drawer>

                <Drawer
                    title="修改密码"
                    width={400}
                    closable={false}
                    onClose={this.onPasswordDrawerClose}
                    visible={this.state.passwordDrawer}
                >
                    <Form
                        {...layout}
                        name="basic"
                        onFinish={this.onFinish}
                    >
                        <Form.Item
                            label="旧密码"
                            name="oldPassword"
                            rules={[
                                {
                                    required: true,
                                    message: '请输旧密码',
                                },
                            ]}
                        >

                            <Input type='text'
                                placeholder="请输入旧密码" />
                        </Form.Item>
                        <Form.Item
                            label="新密码"
                            name="newPassword"
                            rules={[
                                {
                                    compare: '11111',
                                    required: true,
                                    message: '请输新密码',
                                },
                            ]}
                        >

                            <input placeholder="请输新密码" type="text" id="basic_dtsName"
                                class="ant-input" value="" />
                        </Form.Item>

                        <Form.Item {...tailLayout}>
                            <Button type="primary" htmlType="submit" style={{ marginRight: '10px' }}
                            >
                                确定
                                </Button>

                            <Button htmlType="button" onClick={() => this.quit()}>
                                取消
                                </Button>
                        </Form.Item>
                    </Form>
                </Drawer>

            </Drawer>
            <Layout style={{ height: '100%' }}>
                <Header className="header" style={{ position: 'relative', left: '0', padding: '0', background: '#12202e' }}>
                    <Row>
                        <span style={{ color: '#ffffff', textAlign: 'center', width: '200px', fontSize: '20px' }}>DTRUN迁移系统</span>
                        <div onClick={this.showMainDrawer} style={{ position: 'absolute', right: '45px', cursor: 'pointer' }}>
                            <Avatar size={45} src={iconUrl} />
                            <span style={{ marginLeft: '5px', color: '#ffffff', fontSize: '15px' }}>{userName}</span>
                        </div>
                    </Row>

                </Header>
                <Layout>
                    <Sider width={200} className="site-layout-background" style={{
                        overflow: 'auto',
                        height: '100vh',
                        position: 'fixed',
                        left: 0,
                    }}>
                        <Menu
                            mode="inline"
                            // defaultSelectedKeys={['1']}
                            style={{ height: '100%', borderRight: 0 }}
                        >
                            <Menu.Item key="1" icon={<LaptopOutlined />}>
                                <NavLink to="/dashboard">运维大盘</NavLink>
                            </Menu.Item>
                            <Menu.Item key="2" icon={<LaptopOutlined />}>
                                <NavLink to="/datasource/show">数据源管理</NavLink></Menu.Item>
                            <Menu.Item key="3" icon={<LaptopOutlined />}>
                                <NavLink to="/movetask"> 迁移任务管理</NavLink>
                            </Menu.Item>
                            <SubMenu key="sub2" icon={<LaptopOutlined />} title="subnav 2">
                                <Menu.Item key="5">option5</Menu.Item>
                                <Menu.Item key="6">option6</Menu.Item>
                                <Menu.Item key="7">option7</Menu.Item>
                                <Menu.Item key="8">option8</Menu.Item>
                            </SubMenu>
                            <SubMenu key="sub3" icon={<NotificationOutlined />} title="subnav 3">
                                <Menu.Item key="9">option9</Menu.Item>
                                <Menu.Item key="10">option10</Menu.Item>
                                <Menu.Item key="11">option11</Menu.Item>
                                <Menu.Item key="12">option12</Menu.Item>
                            </SubMenu>
                        </Menu>
                    </Sider>
                    <Layout style={{ padding: '0 14px 14px 14px', height: '100%' }}>

                        <Content
                            className="site-layout-background"
                            style={{
                                marginLeft: 200,
                                height: '100%'
                            }}
                        >
                            <Switch>
                                <Route path="/dashboard" component={Dashboard} />
                                <Route path="/datasource" component={Datasource} />
                                <Route path="/movetask" component={MoveTask} />
                                <Redirect to="/dashboard" />
                            </Switch>
                        </Content>
                    </Layout>
                </Layout>
            </Layout>
        </div>
    }
}