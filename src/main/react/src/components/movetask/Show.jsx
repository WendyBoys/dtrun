import React, {useEffect, useState} from 'react';
import {deletes, findAll, quit, run, getResultById} from './service';
import {Button, message, Modal, notification, Popconfirm, Space, Spin, Table, Tag, Tooltip} from 'antd';
import {
    CheckCircleOutlined,
    CloseCircleOutlined,
    DisconnectOutlined,
    FlagFilled,
    SyncOutlined
} from '@ant-design/icons';


const Show = (props) => {
    const [list, setList] = useState([]);
    const [resultList, setResultList] = useState([]);
    const [fileMessageList, setFileMessageList] = useState([]);
    const [taskId, setTaskId] = useState([]);
    const [visibleDelete, setVisibleDelete] = useState(false);
    const [visibleResult, setVisibleResult] = useState(false);
    const [visibleFileMessageList, setVisibleFileMessageList] = useState(false);
    const [loading, setLoading] = useState(false);
    const [resultLoading, setResultLoading] = useState(false);
    const [fileMessagelistloading, setFileMessagelistloading] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [modalText, setModalText] = useState('');
    const [modalResultText, setModalResultText] = useState('');
    const [modalResultId, setmodalResultId] = useState('');
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);


    useEffect(() => {
        fentch(true);
        const interval = setInterval(() => {
            fentch(false)
        }, 5000)
        return () => {
            clearInterval(interval)
        }
    }, []);


    const fentch = (isShow) => {
        if (isShow) {
            setLoading(true)
            findAll().then((response) => {
                const result = response.data.message;
                const data = response.data.data;
                if (result === 'Success') {
                    setList(data);
                    setLoading(false)
                } else {
                    notification['error']({
                        message: '通知',
                        description:
                            '获取任务列表失败',
                        duration: 2,
                    });
                }
            });
        } else {
            findAll().then((response) => {
                const result = response.data.message;
                const data = response.data.data;
                if (result === 'Success') {
                    setList(data);
                } else {
                    notification['error']({
                        message: '通知',
                        description:
                            '获取任务列表失败',
                        duration: 2,
                    });
                }
            });
        }

    }


    const toCreate = () => {
        props.history.push('/movetask/create');
    }

    const toUpdate = (id, status) => {
        if (status === 'RUNNING') {
            message.error("正在运行中的任务无法编辑");
            return;
        }
        props.history.push('/movetask/update/' + id)
    }


    const deleteTask = (id, status, taskName) => {
        if (status === 'RUNNING') {
            message.error("正在运行中的任务无法删除")
            return;
        }
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


    const getResult = (id) => {
        setResultLoading(true)
        getResultById({
            id: id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setResultList(response.data.data)
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '查询失败',
                    duration: 2,
                });
            }
            setResultLoading(false)
        });
    }


    const showResult = (id, taskName) => {
        setModalResultText('迁移任务 ' + taskName + ' 执行结果')
        setVisibleResult(true)
        setmodalResultId(id)
        getResult(id);
    }

    const fenchResult = () => {
        getResult(modalResultId);
    }

    const showFileMessageList = (id) => {
        setVisibleFileMessageList(true)
        setFileMessagelistloading(true)
        getResultById({
            id: id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setFileMessageList(JSON.parse(response.data.data[0].fileMessageList))
            } else {
                notification['error']({
                    message: '通知',
                    description:
                        '查询失败',
                    duration: 2,
                });
            }
            setFileMessagelistloading(false)
        });
    }


    const handleDeteleOk = () => {
        setConfirmLoading(true);
        deletes({
            data: {
                id: taskId,
            }
        }).then((response) => {
            const result = response.data.message;
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


    const handleCancel = () => {
        setVisibleDelete(false);
    };


    const confirm = (id) => {
        setConfirmLoading(true);
        run({
            id: id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setConfirmLoading(false);
                setSelectedRowKeys([])
                fentch();
                message.success('启动成功');
            } else {
                message.error('启动失败');
            }
        });
    }

    const cancel = (id) => {
        setConfirmLoading(true);
        quit({
            id: id,
        }).then((response) => {
            const result = response.data.message;
            if (result === 'Success') {
                setConfirmLoading(false);
                setSelectedRowKeys([]);
                fentch();
                message.success('取消成功');
            } else {
                message.error('取消失败');
            }
        });
    }


    const columns = [
        {
            title: '迁移任务名称',
            dataIndex: 'taskName',
            render: (text, record) => <span style={{color: '#0062FF', cursor: 'pointer', disable: 'true'}}
                                            onClick={() => toUpdate(record.id, record.status)}>{text}</span>,
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
                } else if (text === 'QUIT') {
                    return (
                        <Tag icon={<DisconnectOutlined/>} color="orange">
                            已取消
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
                            <Popconfirm
                                title={'确定取消 ' + record.taskName + ' 的运行吗?'}
                                onConfirm={() => cancel(record.id)}
                                okText="确定"
                                cancelText="取消"
                            >
                                <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}>取消</span>
                            </Popconfirm>
                            :
                            <Popconfirm
                                title={'确定启动 ' + record.taskName + ' 吗?'}
                                onConfirm={() => confirm(record.id)}
                                okText="确定"
                                cancelText="取消"
                            >
                               <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}
                               >启动</span>
                            </Popconfirm>
                        }
                        <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}
                              onClick={() => showResult(record.id, record.taskName)}>查看结果</span>
                        <span style={{color: '#0062FF', cursor: 'pointer', marginRight: '10px'}}
                              onClick={() => deleteTask(record.id, record.status, record.taskName)}>删除</span>
                    </Space>
                )
            }
        },
    ];


    const resultColumns = [
        {
            title: '开始时间',
            dataIndex: 'startTime',
        },
        {
            title: '结束时间',
            dataIndex: 'endTime',
        },
        {
            title: '总耗时',
            dataIndex: 'timeConsume',
            render: (text) => {
                return text + '秒'
            }
        },
        {
            title: '执行结果',
            dataIndex: 'result',
            render: (text, record) => {
                if (text === 'FINISH') {
                    return (
                        <Tag icon={<CheckCircleOutlined/>} color="success">
                            已完成
                        </Tag>
                    )
                } else if (text === 'QUIT') {
                    return (
                        <Tag icon={<DisconnectOutlined/>} color="orange">
                            已取消
                        </Tag>
                    )
                } else {
                    return (
                        <Tag icon={<CloseCircleOutlined/>} color="error">
                            <Tooltip placement="bottom" title={record.failReason}>
                                执行失败
                            </Tooltip>

                        </Tag>
                    )
                }
            }
        },
        {
            title: '迁移文件数量',
            dataIndex: 'fileCount',
        },
        {
            title: '详细信息',
            dataIndex: 'mid',
            render: (text) => {
                return (
                    <span onClick={() => showFileMessageList(text)} style={{color:"blue",cursor: 'pointer'}}>查看</span>
                )
            }
        },
    ];


    const fileMessageListColumns = [
        {
            title: '文件名称',
            dataIndex: 'fileName',
        },
        {
            title: '文件大小',
            dataIndex: 'fileLength',
            render: (text) => {
                return (text/1024).toFixed(2) + 'KB'
            }
        },

    ];


    const onSelectChange = selectedRowKeys => {
        setSelectedRowKeys(selectedRowKeys)
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
        getCheckboxProps: (record: columns) => ({
            disabled: record.status === 'RUNNING',
        }),
    };

    return ((
        <Spin size="large" spinning={loading}>
            <div style={{height: '50px', paddingTop: '10px'}}>
                <Button type="primary" onClick={() => fentch(true)}
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
                    <Table
                        rowSelection={rowSelection}
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
                    title={modalResultText}
                    centered
                    visible={visibleResult}
                    onOk={() => setVisibleResult(false)}
                    onCancel={() => setVisibleResult(false)}
                    width={800}
                    footer={[
                        <Button onClick={() => fenchResult()}>
                            刷新
                        </Button>,
                        <Button type="primary" loading={loading} onClick={() => setVisibleResult(false)}>
                            确定
                        </Button>,
                    ]}
                >
                    <Spin size={"large"} spinning={resultLoading}>
                        <Table
                            bordered
                            columns={resultColumns}
                            dataSource={resultList}
                            pagination={{
                                showQuickJumper: true,
                                defaultPageSize: 10,
                                pageSizeOptions: [10, 20, 50, 100]
                            }}
                        />
                    </Spin>
                </Modal>


                <Modal
                    title="详细信息"
                    centered
                    visible={visibleFileMessageList}
                    width={500}
                    onCancel={() => setVisibleFileMessageList(false)}
                    footer={[
                        <Button type="primary" loading={fileMessagelistloading}
                                onClick={() => setVisibleFileMessageList(false)}>
                            确定
                        </Button>,
                    ]}
                >
                    <Spin size={"large"} spinning={fileMessagelistloading}>
                        <Table
                            bordered
                            columns={fileMessageListColumns}
                            dataSource={fileMessageList}
                            pagination={{
                                showQuickJumper: true,
                                defaultPageSize: 10,
                                pageSizeOptions: [10, 20, 50, 100]
                            }}
                        />
                    </Spin>
                </Modal>

            </div>
        </Spin>
    ));
}


export default Show;
