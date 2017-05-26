package com.zalehacks.apps.internmatch;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private OnLoginListener mCallback;

    private Button mRegisterHereButton;
    private Button mLoginButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private ProgressBar mLoginProgress;

    private String mEnteredEmailString;
    private String mEnteredPassword;


    public interface OnLoginListener {
        public void onLogin(boolean success, int id);
        public void onChangeToRegister();
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnLoginListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnLoginListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mRegisterHereButton = (Button)v.findViewById(R.id.button_register_here);
        mEmailEditText = (EditText)v.findViewById(R.id.edit_text_login_email);
        mPasswordEditText = (EditText)v.findViewById(R.id.edit_text_login_password);
        mLoginButton = (Button)v.findViewById(R.id.button_login);

        mRegisterHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onChangeToRegister();
            }
        });

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEnteredEmailString = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEnteredPassword = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (mEnteredEmailString == null || mEnteredEmailString.trim().equals(""))
                    Toast.makeText(getActivity(), R.string.toast_enter_email, Toast.LENGTH_SHORT).show();
                else if(mEnteredPassword == null || mEnteredPassword.trim().equals(""))
                    Toast.makeText(getActivity(), R.string.toast_enter_password, Toast.LENGTH_SHORT).show();
                else
                    new LoginTask().execute(mEnteredEmailString, mEnteredPassword);

                //Toast.makeText(getActivity(), ""+Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
            }
        });

        mLoginProgress = (ProgressBar)v.findViewById(R.id.progressbar_login);

        return v;
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

    private void loginFail(){
        Toast.makeText(getActivity(), R.string.toast_login_fail, Toast.LENGTH_LONG).show();
    }


    private class LoginTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoginButton.setClickable(false);
            mRegisterHereButton.setClickable(false);
            mEmailEditText.setEnabled(false);
            mPasswordEditText.setEnabled(false);
            mLoginProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... info) {
            Email email = new Email(info[0]);
            String password = info[1];

            Instance.get(getActivity()).free();
            return new Connector(getActivity()).login(email, password, getActivity());

        }

        @Override
        protected void onPostExecute(Boolean loggedin) {
            mLoginProgress.setVisibility(View.GONE);
            if(!loggedin) {
                loginFail();

                mLoginButton.setClickable(true);
                mRegisterHereButton.setClickable(true);
                mEmailEditText.setEnabled(true);
                mPasswordEditText.setEnabled(true);

            }
            mCallback.onLogin(loggedin, Instance.get(getActivity()).getmUser().getmId());

        }
    }


}
