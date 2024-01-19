import React, { useEffect } from 'react'
import { port, protocol } from '../fetchConst';
import useStateRef from 'react-usestateref';
import { useNavigate } from 'react-router-dom';

const ResultPageForAdmin = () => {
    const navigate = useNavigate();
    const [partyId, setPartyId, refPartyId] = useStateRef()
    const [studentListWithPartyId, setStudentListWithPartyId, refStudentListWithPartyId] = useStateRef();
    const [performanceList, setPerformanceList, refperformanceList] = useStateRef();
    const uri = `${protocol}://${window.location.hostname}:${port}`;

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const examId = urlParams.get("examId");

    useEffect(() => {
        getUserList()
    }, []);

    const getUserList = () => {
        fetch(`${uri}/onlineexamapplication/control/get-user-list-for-result?examId=${examId}`,
            { credentials: "include" }
        )
            .then((response) => response.json())
            .then(data => {
                console.log(data)
                setPartyId(data.studentUserLogin.partyId)
                setStudentListWithPartyId(data.studentListWithPartyId)
                setPerformanceList(data.performanceList)
            })
    }
    console.log(refperformanceList.current);
    console.log(refPartyId.current);
    const getResult = (student, studentsName) => {
        console.log(student[studentsName])
        console.log(studentsName)
        navigate(`/result-page-for-admin-and-student?examId=${examId}&&partyIdOfStudent=${student[studentsName]}`);

    }

    return (
        <>
            <div className="card m-5" >
                <div className="card-header">
                    <h3>Students</h3>
                </div>
                {console.log("refStudentListWithPartyId", refStudentListWithPartyId.current)}
                {refStudentListWithPartyId.current && refStudentListWithPartyId.current.map((student) => {
                    { console.log(student) }
                    return (
                        <ul className="list-group list-group-flush">
                            <li className="list-group-item" onClick={() => { getResult(student, Object.keys(student)) }}>{Object.keys(student)}
                                {performanceList.length === 0 ?
                                    <button className="btn btn-outline-success m-1" style={{ 'float': 'right' }}>NotAttempted</button>
                                    : <>
                                        <button className="btn btn-outline-success m-1" style={{ 'float': 'right' }}>Attempted</button>
                                    </>}
                            </li>
                        </ul>
                    )
                })}

            </div>
        </>
    )
}

export default ResultPageForAdmin