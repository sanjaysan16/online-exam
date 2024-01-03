import React, { useEffect, useState } from "react";
import useStateRef from "react-usestateref";
import Swal from "sweetalert2";
const ViewQuestion = () => {
  const [listOfQuestions, setListQuestions, refListQuestions] = useStateRef();
  const[state,setState]=useState(false)

  const queryParamByTopicId=window.location.search;
  const myKeyValuePair=new URLSearchParams(queryParamByTopicId);
  const topicId=myKeyValuePair.get('topicId');
  useEffect(() => {
    fetchCall();
  }, []);
  // get question list fetchcall
  function fetchCall() {
    fetch(`https://localhost:8443/onlineexamapplication/control/view-questions?topicId=${topicId}`, { credentials: "include" })
      .then((res) => res.json())
      .then((data) => {
        var questionList = data.questionList;

        setListQuestions(questionList);
        console.log(refListQuestions.current)
      })
      .catch(error => console.error('Error:', error));
  }

  //  function for delete button
  const deleteQuestion = (id) => {
    var questionId = id;
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: "btn btn-success",
        cancelButton: "btn btn-danger ",
      },
      buttonsStyling: false,
    });
    swalWithBootstrapButtons
      .fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, cancel!",
        reverseButtons: true,
      })
      .then((result) => {
        if (result.isConfirmed) {
          fetch(
            `https://localhost:8443/onlineexamapplication/control/delete-question?questionId=${questionId}`, { credentials: "include" }
          )
            .then((res) => res.json())
            .then((data) => {
              if (data.result == "success") {
                setState(true)
                swalWithBootstrapButtons.fire({
                  title: "Deleted!",
                  text: "Your file has been deleted.",
                  icon: "success",
                  
                });
               setTimeout(function(){
                window.location.reload();
               },2000);
               }
              
            })
            .catch(error => console.error('Error:', error));
        } else if (
          /* Read more about handling dismissals below */
          result.dismiss === Swal.DismissReason.cancel
        ) {
          swalWithBootstrapButtons.fire({
            title: "Cancelled",
            text: "Your imaginary file is safe :)",
            icon: "error",
          });
        }
      });
  };

  return (
    <>
      <div className="container ">
        <div className="card mt-5">
          <div className="card-header text-center text-light custom-card-header" style={{ backgroundColor: '$indigo-900'}}>
            <h2> Question List</h2>
          </div>
          <div className="cardbody mt-1">
            <table className="table  table-hover">
              <thead className="table-light  my-5 ">
                <tr className="">
                  <th>QuestionId</th>
                  <th>QuestionDetail</th>
                  <th>TopicId</th>
                  <th></th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {refListQuestions.current ?
                
                  refListQuestions.current.map((listData) => {
                    return (
                      <>
                        <tr>
                          <td>{listData.questionId}</td>
                          <td>{listData.questionDetail}</td>
                          <td>{listData.topicId}</td>
                          <td>
                            <a
                              href={`update-question?questionId=${listData.questionId}&&topicId=${listData.topicId}&&questionType=${listData.questionType}`}
                            >
                              <button className="btn btn-warning offset-md-2">
                                Edit Question
                              </button>
                            </a>
                            <button
                              className="btn btn-danger offset-md-1"
                              onClick={() =>
                                deleteQuestion(listData.questionId)
                              }
                            >
                              Delete
                            </button>
                          </td>
                        </tr>
                      </>
                    );
                  }):<h2 style={{color:'red'}}>no question in this topic</h2>}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
};

export default ViewQuestion;
