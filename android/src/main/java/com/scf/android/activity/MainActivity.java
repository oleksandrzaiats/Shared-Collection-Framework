package com.scf.android.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.scf.android.R;
import com.scf.android.SCFApplication;
import com.scf.android.adapter.ContentListAdapter;
import com.scf.client.SCFClient;
import com.scf.shared.dto.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbstractActivity {
    private String COLLECTION_ID_EXTRA = "COLLECTION_ID_EXTRA";
    private ContentListAdapter adapter;
    private long currentCollectionId = -1;
    private SCFClient scfClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SCFApplication application = (SCFApplication) MainActivity.this.getApplication();
        scfClient = application.getSCFClient();

        // Getting extra collection ID from intern if there is any
        Intent intent = getIntent();
        if (intent.hasExtra(COLLECTION_ID_EXTRA)) {
            currentCollectionId = intent.getLongExtra(COLLECTION_ID_EXTRA, currentCollectionId);
        }

        // Setting list view adapter and it's click event, registering for context menu
        ListView listView = (ListView) findViewById(R.id.mainListView);
        registerForContextMenu(listView);
        adapter = new ContentListAdapter(createEmptyCollectionDTO(), this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                Object colOrArtDTO = adapter.getItem(position);
                if (colOrArtDTO instanceof CollectionDTO) {
                    Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                    myIntent.putExtra(COLLECTION_ID_EXTRA, ((CollectionDTO) adapter.getItem(position)).getId());
                    MainActivity.this.startActivity(myIntent);
                } else if (colOrArtDTO instanceof ArtifactDTO) {
                    Intent myIntent = new Intent(MainActivity.this, SelectActivity.class);
                    myIntent.putExtra(UpdateActivity.ARTIFACT_ID_EXTRA, ((ArtifactDTO) colOrArtDTO).getId());
                    MainActivity.this.startActivity(myIntent);
                } else {
                    System.err.println("Unknown object was passed to the click event [" + colOrArtDTO.toString() + "].");
                    return;
                }
            }
        });
        listView.setAdapter(adapter);

        // Async task for loading collections
        SCFAsyncTask<CollectionDTO> scfAsyncTask = new SCFAsyncTask<CollectionDTO>() {
            @Override
            void onSuccess(CollectionDTO collection) {
                setTitle(collection.getName());
                adapter.setCollection(collection);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Exception value) {
                super.onFailure(value);
            }

            @Override
            CollectionDTO inBackground() {
                CollectionDTO collectionDTO = null;
                if (currentCollectionId != -1) {
                    collectionDTO = scfClient.getCollection(currentCollectionId);
                } else {
                    collectionDTO = createEmptyCollectionDTO();
                    collectionDTO.getCollectionList().addAll(scfClient.getAllCollections());
                }
                return collectionDTO;
            }
        };
        scfAsyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_collection:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                alert.setView(input);
                alert.setTitle("Creating new collection");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SCFAsyncTask<Boolean> scfAsyncTask = new SCFAsyncTask<Boolean>() {
                                @Override
                                void onSuccess(Boolean value) {
                                    if (value) adapter.notifyDataSetChanged();
                                }

                                @Override
                                protected void onFailure(Exception value) {
                                    super.onFailure(value);
                                }

                                @Override
                                Boolean inBackground() {
                                    CollectionDTO collection = new CollectionDTO();
                                    collection.setName(input.getText().toString());
                                    collection = scfClient.createCollection(collection);
                                    CollectionDTO currentCollection = adapter.getCollection();
                                    currentCollection.getCollectionList();
                                    System.out.println(currentCollection.getCollectionList().getClass());
                                    currentCollection.getCollectionList().add(collection);
                                    if (currentCollection.getId() != -1) {
                                        scfClient.updateCollection(currentCollection);
                                    }
                                    return true;
                                }
                            };
                        scfAsyncTask.execute();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled
                    }
                });

                alert.show();
                return true;
            case R.id.menu_add_artifact:
                Intent myIntent = new Intent(MainActivity.this, CreateActivity.class);
                myIntent.putExtra("collection", adapter.getCollection());
                startActivityForResult(myIntent, Activity.RESULT_OK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        long artifact_id = data.getLongExtra(CreateActivity.ARTIFACT_ID_EXTRA, -1);
        System.out.println(artifact_id);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.mainListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_main_list_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final CommonDTO colOrArtDTO = adapter.getListDTOItem(info.position);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        switch(item.getItemId()) {
            case R.id.menu_rename_list_item:
                final EditText input = new EditText(this);
                alert.setView(input);

                if (colOrArtDTO instanceof CollectionDTO) {
                    alert.setTitle("Changing collection's name");
                    input.setText(((CollectionDTO) colOrArtDTO).getName());
                } else if (colOrArtDTO instanceof ArtifactDTO) {
                    Intent myIntent = new Intent(MainActivity.this, UpdateActivity.class);
                    myIntent.putExtra(UpdateActivity.ARTIFACT_ID_EXTRA, ((ArtifactDTO) colOrArtDTO).getId());
                    MainActivity.this.startActivity(myIntent);
                } else {
                    System.err.println("Unknown object was passed to the click event [" + colOrArtDTO.toString() + "].");
                }

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (colOrArtDTO instanceof CollectionDTO) {
                            final CollectionDTO collectionDTO = (CollectionDTO) colOrArtDTO;
                            collectionDTO.setName(input.getText().toString());

                            SCFAsyncTask<Boolean> scfAsyncTask = new SCFAsyncTask<Boolean>() {
                                @Override
                                void onSuccess(Boolean value) {
                                    if (value) adapter.notifyDataSetChanged();
                                }

                                @Override
                                protected void onFailure(Exception value) {
                                    super.onFailure(value);
                                }

                                @Override
                                Boolean inBackground() {
                                    scfClient.updateCollection(collectionDTO);
                                    return true;
                                }
                            };
                            scfAsyncTask.execute();

                        } else if (colOrArtDTO instanceof ArtifactDTO) {

                        } else {
                            System.err.println("Unknown object was passed to the click event [" + colOrArtDTO.toString() + "].");
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled
                    }
                });

                if (colOrArtDTO instanceof CollectionDTO) alert.show();
                return true;
            case R.id.menu_delete_list_item:
                if (colOrArtDTO instanceof CollectionDTO) {
                    SCFAsyncTask<Boolean> scfAsyncTask = new SCFAsyncTask<Boolean>() {
                        @Override
                        void onSuccess(Boolean value) {
                            if (value) adapter.notifyDataSetChanged();
                        }

                        @Override
                        protected void onFailure(Exception value) {
                            super.onFailure(value);
                        }

                        @Override
                        Boolean inBackground() {
                            scfClient.deleteCollection((CollectionDTO) colOrArtDTO);
                            adapter.getCollection().getCollectionList().remove(colOrArtDTO);
                            return true;
                        }
                    };
                    scfAsyncTask.execute();
                } else if (colOrArtDTO instanceof ArtifactDTO) {
                    alert.setTitle("Deleting artifact");
                    alert.setMessage("Do you want to delete artifact only from this collection or from all collections?");
                    alert.setPositiveButton("From all collections", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            scfClient.deleteArtifact((ArtifactDTO) colOrArtDTO);
                        }
                    });

                    alert.setNegativeButton("Only from this collection", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            adapter.getCollection().getArtifactList().remove(colOrArtDTO);
                            scfClient.updateCollection(adapter.getCollection());
                        }
                    });
                    alert.show();
                } else {
                    System.err.println("Unknown object was passed to the click event [" + colOrArtDTO.toString() + "].");
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private CollectionDTO createEmptyCollectionDTO() {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setId(-1l);
        collectionDTO.setKey("");
        collectionDTO.setName("Root");
        List<CollectionDTO> collectionDTOs = new ArrayList<>();
        List<ArtifactDTO> artifactsDTOs = new ArrayList<>();
        collectionDTO.setCollectionList(collectionDTOs);
        collectionDTO.setArtifactList(artifactsDTOs);
        return collectionDTO;
    }
}
