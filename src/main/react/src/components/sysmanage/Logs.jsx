import React, {useEffect, useState} from 'react';
import {notification, Timeline, Empty, Menu, Dropdown, message} from 'antd';
import {getLogsById, getColorById} from './service';
import {DownOutlined} from '@ant-design/icons';

const Logs = () => {

    const [list, setList] = useState([]);

    useEffect(() => {
        getLogsById()
            .then((response) => {
                const result = response.data.message;
                if (result === 'Success') {
                    setList(response.data.data)
                } else {
                    notification['error']({
                        message: '通知',
                        description:
                            '获取数据源列表失败，请检查数据源配置',
                        duration: 2,
                    });
                }
            });
    }, []);

    const onClick = ({key}) => {
        getColorById({key}).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '查看失败',
                    duration: 1,
                });
            }
        });

    };

    const menu = (
        <Menu onClick={onClick}>
            <Menu.Item key="all">全部日志</Menu.Item>
            <Menu.Item key="green">登录日志</Menu.Item>
            <Menu.Item key="blue">常规日志</Menu.Item>
            <Menu.Item key="orange">取消日志</Menu.Item>
            <Menu.Item key="red">删除日志</Menu.Item>
        </Menu>
    );


    return <div style={{height: '100%', padding: '10px 10px 0 10px'}}>
        <Dropdown overlay={menu}>
            <a className="ant-dropdown-link" onClick={e => e.preventDefault()}>
                筛选日志 <DownOutlined/>
            </a>
        </Dropdown>

        {
            list.length === 0 ? <Empty style={{position: 'absolute', top: '40%', left: '50%'}}/> :
                <Timeline style={{height: '100%', textAlign: 'left', padding: '20px 10px 0 10px', overflowY: ' auto'}}>
                    {
                        list.map((item, index) => {
                            return <Timeline.Item color={item.color}
                                                  key={index}>{item.log}---{item.createTime}</Timeline.Item>
                        })
                    }
                </Timeline>
        }


    </div>


}

export default Logs;