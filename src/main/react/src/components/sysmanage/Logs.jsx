import React, {useEffect, useState} from 'react';
import {notification, Timeline, Empty} from 'antd';
import {getLogsById} from './service';

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


    return <div style={{height: '100%', padding: '10px 10px 0 10px'}}>
        {
            list.length === 0 ? <Empty style={{position:'absolute',top:'40%',left:'50%'}}/> :
                <Timeline style={{height: '100%', textAlign: 'left', padding: '10px 10px 0 10px', overflowY: ' auto'}}>
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