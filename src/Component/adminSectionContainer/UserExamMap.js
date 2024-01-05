import React, { useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import useStateRef from 'react-usestateref';

const UserExamMap = () => {
    const [listOfMappedExams, setListOfMappedExams, refListOfMappedExams] = useStateRef("");
    const [listOfUnMappedExams, setListOfUnMappedExams, refListOfUnMappedExams] = useStateRef("");

    const [hasError, setHasError, hasErrorRef] = useStateRef(false);
    const location = useLocation();
    const gender = location.state.user.gender;

    useEffect(() => {
        getUserExamMappingList();
    }, []);
    const getUserExamMappingList = async () => {
        const userExamMappedFetch = await fetch('https://localhost:8443/onlineexamapplication/control/get-list-of-user-exam-mapping', {
            method: 'POST',
            credentials: "include",
            body: JSON.stringify({ partyIdOfUser: location.state.user.partyId }),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const userExamMappedList = await userExamMappedFetch.json();
        setListOfMappedExams(userExamMappedList.mappedAndUnMappedExams.listOfMappedExams);
        setListOfUnMappedExams(userExamMappedList.mappedAndUnMappedExams.listOfUnMappedExams);
    }


    //validation for
    const chooseExamValidation = (userExamMapping) => {
        if (userExamMapping.length <= 1) {
            document.getElementById('choose-exam-error').classList.remove('d-none')
            document.getElementById('choose-exam-error').classList.add('d-block')
            document.getElementById('choose-exam-error').innerHTML = "*please choose the exam"
            setHasError(true);
        } else {
            document.getElementById('choose-exam-error').classList.remove('d-block')
            document.getElementById('choose-exam-error').classList.add('d-none')
            document.getElementById('choose-exam-error').innerHTML = ""
        }

    }

    const chooseExamOnSubmit=(e)=>{
        setHasError(false);
        e.preventDefault();
        
        var chooseExamForm=new FormData(e.target);
        var chooseExamObject=Object.fromEntries(chooseExamForm.entries());
        
       var userExamMapping =Object.entries(chooseExamObject);       
          chooseExamValidation(userExamMapping); 

         const chooseExamAsync =async()=>{
            const chooseExamFetch = await fetch('https://localhost:8443/onlineexamapplication/control/user-exam-mapping',{
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify(chooseExamObject),
                headers: {
                  'Content-Type': "application/json",
                  'Accept': "application/json"
                }
                });
            const examListJsonData = await chooseExamFetch.json();
            window.location.reload();
         }
        if(!hasErrorRef.current){
            chooseExamAsync();
        }     
       
    }

    return (
        <div className='pt-3 pb-5 '>
            <div className='row px-5 m-0'>
                <div className='col-sm-12 border card p-0 overflow-auto mb-3'>
                    <h4 className='card-header bg-dark text-white'  style={{"z-index":"1020"," position":"sticky","top":"0"}}>Mapped Exams To User {location.state.user.firstName}</h4>
                    {refListOfMappedExams.current.length === 0 ?  <h6 className='text-warning text-center'>*no exams mapped to this user</h6>:
                        <div style={{"height":`${refListOfMappedExams.current.length >6 ?"200px":""}`}}>
                            <p className='mt-2' style={{ "color": "#808B96" }}> *previously allotted exams to {location.state.user.firstName}</p>
                            <ul>
                                {refListOfMappedExams.current&&refListOfMappedExams.current.map((exam) =>
                                    <li>
                                        {exam.examName}
                                    </li>
                                )}
                            </ul>
                        </div>
                    }
                </div>
                <div className='border col-sm-4 card p-0 mb-5'>
                    <div className='card-header p-3 text-white' style={{
                        "backgroundColor": "#48C9B0"
                    }}>
                        <h1>User Details</h1>
                    </div>
                    <div className='card-body p-3'>
                        <label className='fs-5 pb-3 ' style={{ "fontWeight": "bold" }}>UserId : </label>  {location.state.user.userLoginId}<br />
                        <label className='fs-5 py-3' style={{ "fontWeight": "bold" }}>FirstName : </label> {location.state.user.firstName}<br />
                        <label className='fs-5 py-3' style={{ "fontWeight": "bold" }}>LastName : </label> {location.state.user.lastName}<br />
                        <label className='fs-5 py-3' style={{ "fontWeight": "bold" }}>Gender : </label> {gender.toUpperCase() == "M" ? "Male" : gender.toUpperCase() == "F" ? "Female" : "Others"}

                    </div>
                </div>
                <div className='border col-sm-8 card p-0 mb-5'>
                    <div className='card-header p-3 text-white' style={{
                        "backgroundColor": "#5DADE2"
                    }}>
                        <h1>Choose Exam </h1>
                    </div>
                    <div className='card-body pb-4'>

                        <p className='pt-0 px-2 m-0' style={{ "color": "#808B96" }}>Note : Please click on the arrow botton and choose exam for your user.</p>
                        <p className='pt-0 px-2 text-danger d-none m-0' id='choose-exam-error'></p>

                        <form onSubmit={chooseExamOnSubmit}>
                            <div className='accordion pt-0 px-2' id='accordion-exam' >

                                <div className='accordion-item' style={{ "maxWidth": "100%" }}>
                                    <h4 className='accordion-header' id="accordion-heading">
                                        <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#examList-selection" aria-expanded="false">
                                            Choose Exam
                                        </button>
                                    </h4>
                                    <div id="examList-selection" class="accordion-collapse show collapse" aria-labelledby="accordion-heading" data-bs-parent="#accordion-exam" style={{ "overflow-y": "scroll", "maxHeight": "110px" }}>

                                        <div class="accordion-body">

                                            <label>Choose Exam:</label>
                                            {refListOfUnMappedExams.current.length === 0 ? <h6 className='text-warning text-center'>*no exams available to map user</h6> : refListOfUnMappedExams.current&&refListOfUnMappedExams.current.map((exam, index) => {
                                                return (
                                                    <div className='px-5'>
                                                        <input type='checkbox' name={`choose-exam-${index}`} value={exam.examId} /> <text>{exam.examName}</text>
                                                        <input type='hidden' name="partyIdOfUser" value={location.state.user.partyId} />
                                                    </div>

                                                )
                                            })}

                                        </div>

                                    </div>

                                </div>

                            </div>
                            {refListOfUnMappedExams.current.length=== 0 ? "" :
                                <input className='btn border bg-info position-absolute bottom-0 end-0 mx-4 mb-0' type='submit' name="choose-exam-submit" value="submit" />
                            }
                        </form>
                    </div>
                </div>
            </div>
        </div >
    )
}

export default UserExamMap
	