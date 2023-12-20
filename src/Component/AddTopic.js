import useStateRef from "react-usestateref";


const AddTopic = (topicName) => {
  const topicNames=topicName.topicName;
 
  // console.log(topicNames);
 // console.log({topicName});
    const [hasErrorRef,setHasError,refHasError]=useStateRef(false)

    const validatingTopics=(key,value)=>{
        switch(key){
            case "topicName":
                if(value===""){
                    document.getElementById("validateTopicName").classList.remove("d-none");
                    document.getElementById("validateTopicName").classList.add("d-block");
                    document.getElementById("validateTopicName").innerHTML="*Please enter a topic name";
                    setHasError(true)
                }
                break;
            case "topicpercentage":
                if(value===""){
                    document.getElementById("validatingPercentage").classList.remove("d-none");
                    document.getElementById("validatingPercentage").classList.add("d-block");
                    document.getElementById("validatingPercentage").innerHTML="*Please enter a percentage";
                    setHasError(true)
                }
                break;

            case "topicPassPercentage":
                if(value===""){
                    document.getElementById("validatePasspercentage").classList.remove('d-none');
                    document.getElementById("validatePasspercentage").classList.add('d-block');
                    document.getElementById("validatePasspercentage").innerHTML="*Please enter a percentage";
                    setHasError(true)
                }
                break;
            case "topicQuestions":
                if(value===""){
                    document.getElementById("validateTopicQuestions").classList.remove('d-none');
                    document.getElementById("validateTopicQuestions").classList.add('d-block');
                    document.getElementById("validateTopicQuestions").innerHTML="*Please enter a no of questions"
                    setHasError(true)
                }
                
        }
    }
    const onSubmit =(e)=>{
        console.log("this is sub,it")
        e.preventDefault();

        

        const formData=new FormData(e.target);
        let topicName=formData.get("topicName");
        let topicPassPercentage=formData.get("topicPassPercentage")
        let topicpercentage=formData.get("topicpercentage")
        const value=Object.fromEntries(formData.entries());
        Object.entries(value).map(([key,value])=>{
            validatingTopics(key,value);
        })

        console.log(topicName);
        console.log(topicPassPercentage);
        console.log(topicpercentage);

     if(!refHasError.current){
     const fetchData=async()=>{
        console.log("in fetch")
       const res=await fetch("https://localhost:8443/onlineexamapplication/control/CreateTopicMaster",{

        method:'POST',
        credentials:'include',
        body:JSON.stringify({topicName:topicName,topicpercentage:topicpercentage,topicPassPercentage:topicPassPercentage}),
        headers:{
            'Content-Type': "application/json",
            'Accept': "application/json"
        }
        });
         const data=await res.json();
         topicName=data.topicName;
         console.log(data.topicName);
    
        
     }   
     fetchData();
     }
    }
  return (
    <>
    
    <div>
      <div className="modal fade" id="addTopic"  tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            <form onSubmit={onSubmit}>
              <div className='modal-header bg-secondary '>
                <p className='modal-title text-light fs-2'>Add Topic</p>
              </div>
              <div className='modal-body col-sm-10 offset-md-1'>
                <div className='row mt-3 '>
                  <label className='text-dark'for='topicname' ><h5> Topic name </h5></label>
                  <input type='text' className='form-control' id='topicname' name='topicName' placeholder='Topic Name'></input>
                  <p id="validateTopicName" className="d-none text-danger"></p>

                  <label className="text-dark mt-2" for="topicpercentage"><h5>Percentage</h5></label>
                  <input type="text" className="form-control" id="topicpercentage" name="topicpercentage" placeholder="percentage"/>
                  <p id="validatingPercentage" className="d-none text-danger"></p>

                  <label className='text-dark mt-2'for='topicpasspercentage' ><h5> Pass Percentage </h5></label>
                  <input type='text' className='form-control' id='topicpasspercentage' name='topicPassPercentage' placeholder='Pass Percentage'/>
                  <p id="validatePasspercentage" className="d-none text-danger"></p>

                  {/* <label className='text-dark'for='topicquestions' ><h5> Number of Questions </h5></label>
                  <input type='text' className='form-control' id='topicquestions' name='topicQuestions' placeholder='Number of Questions for Topic'/>
                  <p id="validateTopicQuestions" className="d-none text-danger"></p> */}
                </div>
              </div>
              <div className='row mb-2 px-3'>
                <div className='col-4'>
                  <button className='btn btn-secondary text-center ' >Submit</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
     </>

  )
}

export default AddTopic
