import React, { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'
import useStateRef from 'react-usestateref';

const UserExamMap = () => {
    const [examList, setExamList,refExamList] = useStateRef("");
    const [hasError,setHasError,hasErrorRef]=useStateRef(false);
    const location = useLocation();
    const gender = location.state.user.gender;

    useEffect(() => {

        getExams();
    }, []);

    const getExams = async () => {
        const examFetch = await fetch('https://localhost:8443/onlineexamapplication/control/get-exam-or-exam-list',{ credentials: "include" });
        const examListJsonData = await examFetch.json();
        setExamList(examListJsonData.Exam_List);
    }

    
    //validation for
   const chooseExamValidation=(userExamMapping)=>{
        if(userExamMapping.length<=1){
            document.getElementById('choose-exam-error').classList.remove('d-none')
            document.getElementById('choose-exam-error').classList.add('d-block')
            document.getElementById('choose-exam-error').innerHTML="*please choose the exam"  
            setHasError(true); 
        }else{
          document.getElementById('choose-exam-error').classList.remove('d-block')
          document.getElementById('choose-exam-error').classList.add('d-none')
          document.getElementById('choose-exam-error').innerHTML=""
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
            console.log("examListJsonData",examListJsonData)
         }
        if(userExamMapping.length>1){
            chooseExamAsync();
        }     
    }
   
    return (
        <div className='justify-content-sm-evenly py-3 px-2'>
            <div className='row px-5'>
                <div className='border col-sm-4 card p-0 my-5'>
                    <div className='card-header p-3 text-white' style={{
                        "backgroundColor": "#48C9B0 "
                    }}>
                        <h1>User Details</h1>
                    </div>
                    <div className='card-body p-3'>
                        <label className='fs-5 pb-3 ' style={{ "fontWeight": "bold" }}>UserId : </label>  {location.state.userLogin.userLoginId}<br />
                        <label className='fs-5 py-3' style={{ "fontWeight": "bold" }}>FirstName : </label> {location.state.user.firstName}<br />
                        <label className='fs-5 py-3' style={{ "fontWeight": "bold" }}>LastName : </label> {location.state.user.lastName}<br />
                        <label className='fs-5 py-3' style={{ "fontWeight": "bold" }}>Gender : </label> {gender.toUpperCase() == "M" ? "Male" : gender.toUpperCase() == "F" ? "Female" : "Others"}

                    </div>
                </div>
                <div className='border col-sm-8 card p-0 my-5'>
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
                                            {console.log("refExamList.current",refExamList.current)}
                                            {refExamList.current&&refExamList.current.map((exam,index) => {
                                                return (
                                                    <div className='px-5'>
                                                        <input type='checkbox' name={`choose-exam-${index}`} value={exam.examId}/> <text>{exam.examName}</text>
                                                        <input type='hidden' name="partyIdOfUser" value={location.state.userLogin.partyId}/>
                                                    </div>

                                                )
                                            })}

                                        </div>

                                    </div>

                                </div>

                            </div>
                            <input className='btn border bg-info position-absolute bottom-0 end-0 mx-4 mb-0' type='submit' name="choose-exam-submit" value="submit" />

                        </form>
                    </div>
                </div>
            </div>
        </div >
    )
}

export default UserExamMap
