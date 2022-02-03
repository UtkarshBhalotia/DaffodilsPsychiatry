package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.helper.GlobalConst;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FeeStructureFragment extends Fragment {

    View root;
    Activity activity;
    TableLayout tblAll;
    TableRow trCrash;
    TextView txtmod1_topic, txtmod2_topic, txtmod3_topic, txtmod4_topic, txtmod5_topic, txtmod6_topic, txtmod7_topic, txtmod8_topic, txtmod9_topic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_pricing, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        tblAll= root.findViewById(R.id.tblAll);
        trCrash = root.findViewById(R.id.trCrash);

       /* txtmod1_topic = root.findViewById(R.id.mod1_topic);
        txtmod2_topic = root.findViewById(R.id.mod2_topic);
        txtmod3_topic = root.findViewById(R.id.mod3_topic);
        txtmod4_topic = root.findViewById(R.id.mod4_topic);
        txtmod5_topic = root.findViewById(R.id.mod5_topic);
        txtmod6_topic = root.findViewById(R.id.mod6_topic);
        txtmod7_topic = root.findViewById(R.id.mod7_topic);
        txtmod8_topic = root.findViewById(R.id.mod8_topic);
        txtmod9_topic = root.findViewById(R.id.mod9_topic);

        txtmod1_topic.setText("Specific learning disorder, Temperament, Elimination disorders, Child Forensic Psychiatry(POCSO Act), Mood disorders in children , Schizophrenia in children, Intellectual disability, Autism, ADHD, Conduct disorder, ODD, Tourette's syndrome.");
        txtmod2_topic.setText("Schizophrenia, Mood disorders, Anxiety and Grief Disorders, Suicide, Mood Stabilizers, OCD, Body Dysmorphic disorder.");
        txtmod3_topic.setText("Sleep and Psychiatry, Somatoform Disorder, Dissociative Disorders, Factitious Disorders, Gender Identity Disorders, Sexual Disorders, Dementia, Special population in Psychiatry.");
        txtmod4_topic.setText("Criminal Responsibilty and Psychiatry, Fitness to stand trail, MHCA-2017, NDPS, RPWD, Informed consent, Medical Negliganece, Psychological, Autopsy, Testamentary Capacity, Psychiatrist in court, IDEAS, NMHS, NMHP, DMHP.");
        txtmod5_topic.setText("Alcohol, Cannabis & Endocannabinoid system, Opioids, Cocaine, Inhalants, Tobacco, Behavioral Addiction, Dual Diagnosis.");
        txtmod6_topic.setText("Intelligence, Emotional Quotient, Kindling, Imprinting, Theory of Mind, Learning, Memory, Expressed emotions, Coping Strategies, Defense Mechanism, Freud, Neofreud theories, Personality Testing.");
        txtmod7_topic.setText("Epilepsy, Consultation Liasion, Ect, rTMS, Soft Neurological Signs, Vascular Depression, Neurotransmitters and receptors, Limbic systems, Cerebral Dominance, Phantom Limb, Sterios Induced Psychosis, Depression in clinical setting, Headache.");
        txtmod8_topic.setText("Detailed mental status examination, Psychopathology discussion, Cases on Schizophrenia, Bipolace disorder, Elderly Depression, Substance use disorder, Explanation of Token Economy, Lithium toxicity, Cognitive Behaviour therapy, Kirby's method, Treatment Substance Schizophrenia.");
        txtmod9_topic.setText("Pathways and receptors of Dopamine, Glutamate, GABA; NMDA Hypo functioning hypothesis of Schizophrenia, Serotonergic pathways and receptors, Hypnosis, anti-manic, antidepressants, antipsychotics, Clozapine, Individual antipsychotic drugs including Newer 3rd Gen antipsychotics, Depression and mania, SSRIs in detail, SPARI, SNRIs, Agomelatine, alpha blockers, SARIs,TCAs, Vortioxetin, Mood stabilizers, Lithium, Valproate and other mood stabilizers including special considerations, ADHD including individual drugs, Addiction, impulsivity, compulsivity including individual drugs, anxiety.");
*/

        tblAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FoundationCourseFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "FoundationCourse");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
            }
        });

        trCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CrashCourseFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "CrashCourse");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
            }
        });
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_pricing);
        getActivity().invalidateOptionsMenu();
        hideKeyboard();
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(root.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
