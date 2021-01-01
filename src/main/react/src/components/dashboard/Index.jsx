import React from 'react';
import {NavLink, Redirect, Route, Switch} from 'react-router-dom';
import Dashboard from './Dashboard';
import Datasource from '../datasource/Datasource';
import MoveTask from '../movetask/MoveTask';
import {Layout, Menu} from 'antd';
import {LaptopOutlined, NotificationOutlined} from '@ant-design/icons';

const { SubMenu } = Menu;
const { Header, Content, Sider } = Layout;

export default class Index extends React.Component {
    render() {
        return <div style={{ height: '100%' }}>
            <Layout style={{ height: '100%' }}>
                <Header className="header" style={{ position: 'relative', left: '0', padding: '0', background: '#12202e' }}>
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
                            defaultSelectedKeys={['1']}
                            defaultOpenKeys={['sub1']}
                            style={{ height: '100%', borderRight: 0 }}
                        >
                            <Menu.Item key="1" icon={<LaptopOutlined />}><NavLink to="/dashboard">
                                运维大盘
                </NavLink></Menu.Item>
                            <Menu.Item key="2" icon={<LaptopOutlined />}><NavLink to="/datasource/show">
                                数据源管理
                </NavLink></Menu.Item>
                            <Menu.Item key="3" icon={<LaptopOutlined />}><NavLink to="/movetask">
                                迁移任务管理
                </NavLink></Menu.Item>
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