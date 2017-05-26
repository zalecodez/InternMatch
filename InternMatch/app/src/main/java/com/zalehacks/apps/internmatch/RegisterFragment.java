package com.zalehacks.apps.internmatch;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private User mUser = new User();
    private String mEmailString;
    private String mGpaString;

    private RadioButton mGenderRadioButton;
    private RadioButton mLevelRadioButton;

    private Button mLoginHereButton;
    private RadioGroup mTypeRadioGroup;
    private RadioGroup mGenderRadioGroup;
    private RadioGroup mLevelRadioGroup;
    private EditText mGpaEdit;

    private EditText mNameEdit;
    private EditText mFnameEdit;
    private EditText mLnameEdit;
    private EditText mEmailEdit;
    private EditText mPasswordEdit;
    private Button mRegisterButton;

    private ProgressBar mRegisterProgress;

    private RegisterListener mCallback;

    private boolean mRegistered;




    public interface RegisterListener{
        void onChangeToLogin(boolean registered);
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (RegisterListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement RegisterListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mLoginHereButton = (Button)v.findViewById(R.id.button_login_here);


        mTypeRadioGroup = (RadioGroup)v.findViewById(R.id.radio_group_type);
        mGenderRadioGroup = (RadioGroup)v.findViewById(R.id.radio_group_gender);
        mLevelRadioGroup = (RadioGroup)v.findViewById(R.id.radio_group_level);

        mGpaEdit = (EditText)v.findViewById(R.id.edit_text_register_gpa);

        mNameEdit = (EditText)v.findViewById(R.id.edit_text_register_name);
        mFnameEdit = (EditText)v.findViewById(R.id.edit_text_register_fname);
        mLnameEdit = (EditText)v.findViewById(R.id.edit_text_register_lname);
        mEmailEdit = (EditText)v.findViewById(R.id.edit_text_register_email);
        mPasswordEdit = (EditText)v.findViewById(R.id.edit_text_register_password);
        mRegisterButton = (Button)v.findViewById(R.id.button_register);

        mLoginHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onChangeToLogin(false);
            }
        });



        changeType(mTypeRadioGroup.getCheckedRadioButtonId());
        mTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                changeType(i);
            }
        });

        mGenderRadioButton = (RadioButton) v.findViewById(mGenderRadioGroup.getCheckedRadioButtonId());
        mUser.setmGender(mGenderRadioButton.getText().toString());
        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                mGenderRadioButton = (RadioButton)radioGroup.findViewById(i);
                mUser.setmGender(mGenderRadioButton.getText().toString());
            }
        });

        mLevelRadioButton = (RadioButton) v.findViewById(mLevelRadioGroup.getCheckedRadioButtonId());
        mUser.setmLevel(mLevelRadioButton.getText().toString());
        mLevelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                mLevelRadioButton = (RadioButton)radioGroup.findViewById(i);
                mUser.setmLevel(mLevelRadioButton.getText().toString());
            }
        });

        mGpaEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mGpaString = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUser.setmName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mFnameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUser.setmFname(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLnameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUser.setmLname(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEmailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmailString = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUser.setmPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((mUser.getmFname() == null || mUser.getmFname().trim().equals(""))
                        && mUser.getmType() == User.Type.student)
                    Toast.makeText(getActivity(), R.string.toast_enter_fname, Toast.LENGTH_SHORT).show();
                else if ((mUser.getmLname() == null || mUser.getmLname().trim().equals(""))
                        && mUser.getmType() == User.Type.student)
                    Toast.makeText(getActivity(), R.string.toast_enter_lname, Toast.LENGTH_SHORT).show();
                else if((mUser.getmName() == null || mUser.getmName().trim().equals(""))
                    && mUser.getmType() == User.Type.company)
                    Toast.makeText(getActivity(), R.string.toast_enter_name, Toast.LENGTH_SHORT).show();
                else if (mEmailString == null || mEmailString.trim().equals(""))
                    Toast.makeText(getActivity(), R.string.toast_enter_email, Toast.LENGTH_SHORT).show();
                else if (mUser.getmPassword() == null || mUser.getmPassword().trim().equals(""))
                    Toast.makeText(getActivity(), R.string.toast_enter_password, Toast.LENGTH_SHORT).show();
                else {
                    mUser.setEmail(new Email(mEmailString));
                    mUser.setmGpa(Float.parseFloat(mGpaString));
                    new RegisterTask().execute(mUser);
                }
            }
        });

        mRegisterProgress = (ProgressBar)v.findViewById(R.id.progressbar_register);

        return v;
    }

    private void changeType(int id){
        if(id == R.id.radio_company){
            mUser.setmType(User.Type.company);
            mNameEdit.setVisibility(View.VISIBLE);
            mFnameEdit.setVisibility(View.GONE);
            mLnameEdit.setVisibility(View.GONE);

            mGenderRadioGroup.setVisibility(View.GONE);
            mLevelRadioGroup.setVisibility(View.GONE);
            mGpaEdit.setVisibility(View.GONE);

        }
        else{
            mUser.setmType(User.Type.student);
            mNameEdit.setVisibility(View.GONE);
            mFnameEdit.setVisibility(View.VISIBLE);
            mLnameEdit.setVisibility(View.VISIBLE);

            mGenderRadioGroup.setVisibility(View.VISIBLE);
            mLevelRadioGroup.setVisibility(View.VISIBLE);
            mGpaEdit.setVisibility(View.VISIBLE);

        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.start_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_cookie:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.menu_item_about:
                Intent i = AboutActivity.newIntent(getActivity());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private class RegisterTask extends AsyncTask<User, Void, Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRegisterButton.setClickable(false);
            mLoginHereButton.setClickable(false);
            mRegisterProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(User... users) {

            return new Connector(getActivity()).register(users[0]);

        }

        @Override
        protected void onPostExecute(Boolean registered) {
            mRegisterProgress.setVisibility(View.GONE);
            if(registered) {
                mRegistered = true;
                Toast.makeText(getActivity(), R.string.registration_success, Toast.LENGTH_SHORT);
                mCallback.onChangeToLogin(mRegistered);
            }
            else{
                mRegisterButton.setClickable(true);
                mLoginHereButton.setClickable(true);
                Toast.makeText(getActivity(), R.string.registration_error, Toast.LENGTH_SHORT);
            }
        }
    }

}
