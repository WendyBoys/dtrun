import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Button, Modal, notification, Space, Spin, Table, Tag} from 'antd';
import {CheckCircleOutlined, CloseCircleOutlined, FlagFilled, SyncOutlined} from '@ant-design/icons';


const Show = (props) => {
    const [list, setList] = useState([]);
    const [taskId, setTaskId] = useState([]);
    const [visibleDelete, setVisibleDelete] = useState(false);
    const [visibleRunTask, setVisibleRunTask] = useState(false);
    const [loading, setLoading] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [modalText, setModalText] = useState('');
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);


    useEffect(() => {
        fentch();
    }, []);


    const fentch = () => {
        setLoading(true)
        axios.get('/movetask/findAll').then((response) => {
            var result = response.data.message;
            var data = response.data.data;
            if (result === 'Success') {
                setList(data);
                setLoading(false)
            } else {
                setLoading(false)
            }
        });
    }


    const toCreate = () => {
        props.history.push('/movetask/create');
    }

    const toUpdate = (id) => {
        props.history.push('/movetask/update/' + id)
    }


    const deleteTask = (id, taskName) => {
        const selecteLength = selectedRowKeys.length;
        if (selecteLength <= 1) {
            setModalText('您确定要删除迁移任务 ' + taskName + ' 吗?');
            const idArray = [];
            idArray.push(id)
            setTaskId(idArray);
        } else {
            setModalText('您确定要删除选中的' + selecteLength + '项迁移任务吗?');
            setTaskId(selectedRowKeys);
        }
        setVisibleDelete(true);
    }

    const runTask = (id, taskName) => {
        const selecteLength = selectedRowKeys.length;
        if (selecteLength <= 1) {
            setModalText('您确定要启动迁移任务 ' + taskName + ' 吗?');
            const idArray = [];
            idArray.push(id)
            setTaskId(idArray);
        } else {
            setModalText('您确定要启动选中的' + selecteLength + '项迁移任务吗?');
            setTaskId(selectedRowKeys);
        }
        setVisibleRunTask(true);
    }

    const handleDeteleOk = () => {
        setConfirmLoading(true);
        axios.delete('/movetask/delete', {
            data: {
                id: taskId,
            }
        }).then((response) => {
            var result = response.data.message;
            if (result === 'Success') {
                setVisibleDelete(false);
                setConfirmLoading(false);
                setSelectedRowKeys([])
                fentch();
                notification['success']({
                    message: '通知',
                    description:
                        '删除成功',
                    duration: 1,
                });
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '删除失败',
                    duration: 1,
                });
            }
        });
    };


    const handleRunTaskOk = () => {
        setConfirmLoading(true);
        axios.post('/movetask/run', {
            id: taskId,
        }).then((response) => {
            var result = response.data.message;
            if (result === 'Success') {
                setVisibleRunTask(false);
                setConfirmLoading(false);
                setSelectedRowKeys([])
                notification['success']({
                    message: '通知',
                    description:
                        '启动成功',
                    duration: 1,
                });
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '启动失败',
                    duration: 1,
                });
            }
        });
    };


    const handleCancel = () => {
        setVisibleDelete(false);
    };

    const handleCancelRunTask = () => {
        setVisibleRunTask(false);
    };


    const columns = [
        {
            title: '迁移任务名称',
            dataIndex: 'taskName',
            render: (text, record) => <span style={{color: '#0062FF', cursor: 'pointer'}}
                                            onClick={() => toUpdate(record.id)}>{text}</span>,
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
        },
        {
            title: '更新时间',
            dataIndex: 'updateTime',
        },
        {
            title: '当前状态',
            dataIndex: 'status',
            render: (text, record) => {
                if (text === 'READY') {
                    return (
                        <Tag icon={<FlagFilled/>} color="cyan">
                            准备就绪
                        </Tag>
                    )
                } else if (text === 'FINISH') {
                    return (
                        <Tag icon={<CheckCircleOutlined/>} color="success">
                            已完成
                        </Tag>
                    )
                } else if (text === 'RUNNING') {
                    return (
                        <Tag icon={<SyncOutlined spin/>} color="processing">
                            运行中
                        </Tag>
                    )
                } else {
                    return (
                        <Tag icon={<CloseCircleOutlined/>} color="error">
                            执行失败
                        </Tag>
                    )
                }
            }
        },
        {
            title: '执行次数',
            dataIndex: 'runCount',
        },
        {
            title: '操作',
            dataIndex: 'option',
            render: (text, record) => {
                return (
                    <Space size="middle">
                        {record.status === 'RUNNING' ?
                            <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}>取消</span> :
                            <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}
                                  onClick={() => runTask(record.id, record.taskName)}>启动</span>}
                        <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}>查看结果</span>
                        <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}
                              onClick={() => deleteTask(record.id, record.taskName)}>删除</span>
                    </Space>
                )
            }
        },
    ];


    const onSelectChange = selectedRowKeys => {
        setSelectedRowKeys(selectedRowKeys)
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    return ((
        <Spin size="large" spinning={loading}>
            <div style={{height: '50px', paddingTop: '10px'}}>
                <Button type="primary" onClick={() => fentch()}
                        style={{
                            background: '#00b4ed',
                            position: 'absolute',
                            right: '83px',
                            zIndex: '999',
                            borderRadius: '5px'
                        }}>刷新</Button>
                <Button type="primary" onClick={() => toCreate()}
                        style={{
                            background: '#00b4ed',
                            position: 'absolute',
                            right: '11px',
                            zIndex: '999',
                            borderRadius: '5px'
                        }}>创建</Button>
            </div>
            <div style={{height: '10px', background: '#f0f2f5'}}></div>
            <div style={{padding: '10px', background: '#ffffff'}}>
                <div>
                    <Table rowSelection={rowSelection}
                           bordered
                           columns={columns}
                           dataSource={list}
                           pagination={{
                               showQuickJumper: true,
                               defaultPageSize: 10,
                               pageSizeOptions: [10, 20, 50, 100]
                           }}
                    />
                </div>

                <Modal
                    title="删除迁移任务"
                    okText="确定"
                    cancelText="取消"
                    visible={visibleDelete}
                    onOk={handleDeteleOk}
                    confirmLoading={confirmLoading}
                    onCancel={handleCancel}
                >
                    <p>{modalText}</p>
                </Modal>

                <Modal
                    title="启动迁移任务"
                    okText="确定"
                    cancelText="取消"
                    visible={visibleRunTask}
                    onOk={handleRunTaskOk}
                    confirmLoading={confirmLoading}
                    onCancel={handleCancelRunTask}
                >
                    <p>{modalText}</p>
                </Modal>
            </div>
        </Spin>

    ));

}


export default Show;